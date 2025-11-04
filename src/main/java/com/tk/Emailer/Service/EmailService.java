package com.tk.Emailer.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.Emailer.Entity.EmailEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
public class EmailService {
    private final WebClient webClient;
    private final String apiKey;

    public EmailService(WebClient.Builder webClient, @Value("${GEMINI_API_KEY}") String apiKey, @Value("${GEMINI_API_URL}") String baseURL) {
        this.apiKey = apiKey;
        this.webClient = webClient.baseUrl(baseURL).build();

    }

    public String responseGenService(EmailEntity emailEntity){
        System.out.println("Response API Called");
        //gen prompt
        String prompt=generatePrompt(emailEntity);

        //make raw jason body
        String reqBody= String.format("""
                {
                     "contents": [
                       {
                         "parts": [
                           {
                             "text": "%s"
                           }
                         ]
                       }
                     ]
                   }
                   
                  """,prompt);
        //send req-> So we get response that we will store her
        String response=getResponse(reqBody);


        //show to user i.e extract the response only from json
        String extractResponse=extractResponse(response);

        return extractResponse;
    }

    private String extractResponse(String response) {
        //So here we will do json NAVIGATION using Object MApper

        try {
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode root=objectMapper.readTree(response);

            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private String getResponse(String reqBody) {



        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .build())
                .header("x-goog-api-key",apiKey)
                .header("Content-Type","application/json")
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private String generatePrompt(EmailEntity emailEntity) {

        StringBuilder prompt=new StringBuilder();
        if(emailEntity.getTone()!=null&& !emailEntity.getTone().isEmpty()){
            prompt.append("Generate a email response in tone");
            prompt.append(emailEntity.getTone());
        }
        prompt.append("Original mail:\n");
        prompt.append(emailEntity.getMailContent());
        return prompt.toString();
    }


}
