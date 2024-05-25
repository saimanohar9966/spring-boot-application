package com.eleven.MongoConnApp.config;

import com.eleven.MongoConnApp.utils.Decryptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;
import com.mongodb.client.MongoClients;
import java.util.Map;

import static com.eleven.MongoConnApp.utils.Decryptor.decryptDBURL;

@Configuration
public class AwsSecretManagerConfig {

    private final String SEVEN_NOW_DB_NAME;
    private final String SEVEN_MP_DB_NAME;
    private final String UNLOCK_KEY;
    private final String DB_URI_SECRET_NAME;
    private final String SECRET_KEY_7MP;
    private final String SECRET_KEY_7NOW;

    AwsSecretManagerConfig(
            @Value("${aws.mongo-conn.secretname}") final String dbUriSecretName,
            @Value("${7NOW.dbname}") final String SevenNowDbName,
            @Value("${7MP.dbname}") final String SevenMPDbName,
            @Value("${encryption.unlock-key}") final String UnlockKey,
            @Value("${7NOW.secretname.key}") final String SevenNowSecretKey,
            @Value("${7MP.secretname.key}") final String SevenMPSecretKey
    ){
        this.SEVEN_MP_DB_NAME = SevenMPDbName;
        this.SEVEN_NOW_DB_NAME = SevenNowDbName;
        this.UNLOCK_KEY = UnlockKey;
        this.DB_URI_SECRET_NAME = dbUriSecretName;
        this.SECRET_KEY_7MP = SevenMPSecretKey;
        this.SECRET_KEY_7NOW = SevenNowSecretKey;
    }


    @Bean
    @Primary
    public Map<String, String> getAwsSecretMap() throws JsonProcessingException {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(DB_URI_SECRET_NAME)
                .build();
        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secretJson = getSecretValueResponse.secretString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> secretMap = objectMapper.readValue(secretJson, Map.class);
        return secretMap;
    }

    @Bean(name="mongoTemplate7MP")
    @Primary
    public MongoTemplate mongoTemplate7MP(Map<String, String> secretMap) throws Exception {
        try {
            String DecryptedMongoUri = Decryptor.decryptDBURL(UNLOCK_KEY, secretMap.get(SECRET_KEY_7MP));
            return new MongoTemplate(MongoClients.create(DecryptedMongoUri), SEVEN_MP_DB_NAME);
        } catch (SecretsManagerException e) {
            throw new RuntimeException("Failed to retrieve secrets from AWS Secrets Manager", e);
        }
    }

    @Bean(name="mongoTemplate7NOW")
    public MongoTemplate mongoTemplate7NOW(Map<String, String> secretMap) throws Exception {
        try {
            String DecryptedMongoUri = Decryptor.decryptDBURL(UNLOCK_KEY, secretMap.get(SECRET_KEY_7NOW));
            return new MongoTemplate(MongoClients.create(DecryptedMongoUri), SEVEN_NOW_DB_NAME);
        } catch (SecretsManagerException e) {
            throw new RuntimeException("Failed to retrieve secrets from AWS Secrets Manager", e);
        }
    }

}
