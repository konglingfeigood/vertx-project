package com.kong.vertx.service;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;

public class FuturePromiseVerticle extends AbstractVerticle {

    //声明Router
    Router router;

    //配置连接参数
    MySQLConnectOptions connectOptions;

    //配置连接池 Pool options
    PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

    //Create the client pool
    MySQLPool client;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(ar -> {
            if (ar.failed()) {
                // Failed to retrieve the configuration
            } else {
                JsonObject config = ar.result();
                String database = config.getString("database");
                System.out.println(database);

                connectOptions = new MySQLConnectOptions()
                        .setPort(config.getInteger("port"))
                        .setHost(config.getString("host"))
                        .setDatabase(config.getString("database"))
                        .setUser(config.getString("user"))
                        .setPassword(config.getString("password"));

                client = MySQLPool.pool(vertx, connectOptions, poolOptions);

                //初始化Router
                router = Router.router(vertx);

                //配置Router解析url
                router.route("/").handler(
                        req -> {
                            req.response()
                                    .putHeader("content-type", "text/plain")
                                    .end("Hello from Vert.x!");
                        }
                );

                //配置Router解析url
                router.route("/test/list").handler(
                        req -> {
                            var page = (Integer.valueOf(req.request().getParam("page")) - 1) * 10;
                            var size = Integer.valueOf(req.request().getParam("size"));
                            System.out.println(page);
                            System.out.println(size);
                            //Get a connection from the pool
                            this.getCon()
                                    .compose(con -> this.getRows(con, page, size))
                                    .onSuccess(row -> {
                                        var list = new ArrayList<JsonObject>();
                                        row.forEach(item -> {
                                            var json = new JsonObject();
                                            json.put("id", item.getValue("id"));
                                            json.put("name", item.getValue("name"));
                                            json.put("phone", item.getValue("phone"));
                                            list.add(json);
                                        });
                                        req.response()
                                                .putHeader("content-type", "application/json")
                                                .end(list.toString());
                                    });
                        }
                );

                //将Router与vertx HttpServer 绑定
                vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
            }
        });
    }

    /**
     * 第一步 获取数据库链接
     *
     * @return
     */
    private Future<SqlConnection> getCon() {
        Promise<SqlConnection> promise = Promise.promise();
        client.getConnection(ar1 -> {
            if (ar1.succeeded()) {
                System.out.println("Connected");

                //Obtain our connection
                SqlConnection conn = ar1.result();
                promise.complete(conn);
            } else {
                promise.fail(ar1.cause());
            }
        });
        return promise.future();
    }

    /**
     * 第二步 用获取到的链接查询数据库
     *
     * @param conn
     * @param page
     * @param size
     * @return
     */
    private Future<RowSet<Row>> getRows(SqlConnection conn, Integer page, Integer size) {
        Promise<RowSet<Row>> promise = Promise.promise();
        conn
                .preparedQuery("select * from youke_user limit ?, ?")
                .execute(Tuple.of(page, size), ar2 -> {
                    //Release the connection to the pool
                    conn.close();

                    if (ar2.succeeded()) {
                        promise.complete(ar2.result());
                    } else {
                        promise.fail(ar2.cause());
                    }
                });
        return promise.future();
    }
}