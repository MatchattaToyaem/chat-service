package edu.oconnor.chatservice.service;

import edu.oconnor.chatservice.grpc.HuggingFaceServiceGrpc;
import edu.oconnor.chatservice.grpc.InferenceReply;
import edu.oconnor.chatservice.grpc.PromptRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ChatbotIntegrationService {

    @GrpcClient("chatbot-server")
    private HuggingFaceServiceGrpc.HuggingFaceServiceBlockingStub huggingFaceStub;

    public InferenceReply askChatbot(String userText) {
        PromptRequest request = PromptRequest.newBuilder()
                .setPrompt(userText)
                .build();
        return huggingFaceStub.generateResponse(request);
    }
}
