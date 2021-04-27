package Server.Communication.example;


  import Server.Domain.CommonClasses.Response;
  import Server.Service.CommerceService;
  import io.netty.buffer.ByteBuf;
  import io.netty.buffer.ByteBufUtil;
  import io.netty.buffer.Unpooled;
  import io.netty.channel.ChannelFuture;
  import io.netty.channel.ChannelFutureListener;
  import io.netty.channel.ChannelHandlerContext;
  import io.netty.channel.ChannelPipeline;
  import io.netty.channel.SimpleChannelInboundHandler;
  import io.netty.handler.codec.http.*;
  import io.netty.handler.ssl.SslHandler;
  import io.netty.util.CharsetUtil;

  import static io.netty.handler.codec.http.HttpHeaderNames.*;
  import static io.netty.handler.codec.http.HttpMethod.*;
  import static io.netty.handler.codec.http.HttpResponseStatus.*;

          /**
    * Outputs index page content.
    */
          public class WebSocketIndexPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

              private final String websocketPath;

              @Override
              public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

                  System.out.println("Client connected." + ctx);

                  super.channelRegistered(ctx);
              }

              @Override
              public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                  System.out.println("Client disconnected." + ctx);
                  super.channelUnregistered(ctx);
              }

              public WebSocketIndexPageHandler(String websocketPath) {
                  this.websocketPath = websocketPath;
              }
              @Override
      protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
                  // Handle a bad request.
                  if (!req.decoderResult().isSuccess()) {
                          sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST,
                                                                                         ctx.alloc().buffer()));
                          return;
                      }
        
                  // Allow only GET methods.
                  if (!GET.equals(req.method())) {
                          sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN,
                                                                                         ctx.alloc().buffer()));
                          return;
                      }
        
                  // Send the index page
                  if ("/".equals(req.uri()) || "/index.html".equals(req.uri())) {
                          String webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath);
                            ByteBuf content = Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8);
                          FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);
            
                          res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-");
                          HttpUtil.setContentLength(res, content.readableBytes());
            
                          sendHttpResponse(ctx, req, res);
                      } else {
                          sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), NOT_FOUND,
                                                                                         ctx.alloc().buffer()));
                      }
              }

              @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                  System.out.println("asaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                  cause.printStackTrace();
                  ctx.close();
              }

              private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
                  // Generate an error page if response getStatus code is not OK ().
                  HttpResponseStatus responseStatus = res.status();
                  if (responseStatus.code() != 200) {
                          ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
                          HttpUtil.setContentLength(res, res.content().readableBytes());
                      }
                  // Send the response and close the connection if necessary.
                  boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
                  HttpUtil.setKeepAlive(res, keepAlive);
                  ChannelFuture future = ctx.writeAndFlush(res);
                  if (!keepAlive) {
                          future.addListener(ChannelFutureListener.CLOSE);
                     }
             }

             private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
                 String protocol = "ws";
                 if (cp.get(SslHandler.class) != null) {
                         // SSL in use so use Secure WebSockets
                         protocol = "wss";
                     }
                 return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
             }
 }
