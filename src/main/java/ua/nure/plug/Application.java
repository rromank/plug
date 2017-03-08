package ua.nure.plug;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public EmbeddedServletContainerCustomizer jettyCustomizer() {
//        return container -> {
//            if (container instanceof JettyEmbeddedServletContainerFactory) {
//                ((JettyEmbeddedServletContainerFactory) container)
//                        .addServerCustomizers(new JettyServerCustomizer() {
//
//                            @Override
//                            public void customize(Server server) {
//                                setHandlerMaxHttpPostSize(200 * 1024 * 1024, server.getHandlers());
//                            }
//
//                            private void setHandlerMaxHttpPostSize(int maxHttpPostSize,
//                                                                   Handler... handlers) {
//                                for (Handler handler : handlers) {
//                                    if (handler instanceof ContextHandler) {
//                                        ((ContextHandler) handler)
//                                                .setMaxFormContentSize(maxHttpPostSize);
//                                    } else if (handler instanceof HandlerWrapper) {
//                                        setHandlerMaxHttpPostSize(maxHttpPostSize,
//                                                ((HandlerWrapper) handler).getHandler());
//                                    } else if (handler instanceof HandlerCollection) {
//                                        setHandlerMaxHttpPostSize(maxHttpPostSize,
//                                                ((HandlerCollection) handler).getHandlers());
//                                    }
//                                }
//                            }
//                        });
//            }
//        };
//    }

}
