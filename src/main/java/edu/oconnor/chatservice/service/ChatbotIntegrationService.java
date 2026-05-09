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

    public InferenceReply askChatbot(String userText, String sessionId) {
        PromptRequest.Builder builder = PromptRequest.newBuilder().setPrompt(userText);
        if (sessionId != null && !sessionId.isBlank()) {
            builder.setSessionId(sessionId);
        }
        return huggingFaceStub.generateResponse(builder.build());
    }
}
