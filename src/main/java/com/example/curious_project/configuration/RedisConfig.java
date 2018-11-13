package com.example.curious_project.configuration;


import com.example.curious_project.Receiver.RedisReceiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory,
                                            @Qualifier("chatMessageListenerAdapter")MessageListenerAdapter chatMessageListenerAdapter,
                                            @Qualifier("friendRequestSentListenerAdapter")MessageListenerAdapter friendRequestSentListenerAdapter,
                                            @Qualifier("friendRequestAcceptedListenerAdapter")MessageListenerAdapter friendRequestAcceptedListenerAdapter,
                                            @Qualifier("friendRequestCancelledListenerAdapter")MessageListenerAdapter friendRequestCancelledListenerAdapter,
                                            @Qualifier("addContactListenerAdapter")MessageListenerAdapter addContactListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(chatMessageListenerAdapter, new PatternTopic("chat"));
        container.addMessageListener(friendRequestSentListenerAdapter, new PatternTopic("friendRequestSent"));
        container.addMessageListener(friendRequestAcceptedListenerAdapter, new PatternTopic("friendRequestAccepted"));
        container.addMessageListener(friendRequestCancelledListenerAdapter, new PatternTopic("friendRequestCancelled"));
        container.addMessageListener(addContactListenerAdapter, new PatternTopic("addContact"));
        return container;
    }

    @Bean("chatMessageListenerAdapter")
    MessageListenerAdapter chatMessageListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveChatMessage");
    }

    @Bean("friendRequestSentListenerAdapter")
    MessageListenerAdapter friendRequestSentListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveSentFriendRequest");
    }

    @Bean("friendRequestAcceptedListenerAdapter")
    MessageListenerAdapter friendRequestAcceptedListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveAcceptedFriendRequest");
    }

    @Bean("friendRequestCancelledListenerAdapter")
    MessageListenerAdapter friendRequestCancelledListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveCancelledFriendRequest");
    }

    @Bean("addContactListenerAdapter")
    MessageListenerAdapter addContactListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveAddContact");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
