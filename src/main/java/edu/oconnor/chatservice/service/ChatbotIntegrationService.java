package edu.oconnor.chatservice.service;


import edu.oconnor.chatservice.grpc.HuggingFaceServiceGrpc;
import edu.oconnor.chatservice.grpc.InferenceReply;
import edu.oconnor.chatservice.grpc.PromptRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ChatbotIntegrationService {
    // The string "chatbot-server" MUST match what you wrote in application.properties
    @GrpcClient("chatbot-server")
    private HuggingFaceServiceGrpc.HuggingFaceServiceBlockingStub huggingFaceStub;

    /**
     * Sends a prompt to the HuggingFace gRPC service and returns the result.
     */
    public String askChatbot(String userText) {

        // 1. Build the Protobuf request object using the generated Builder
        PromptRequest request = PromptRequest.newBuilder()
                .setPrompt(userText)
                .build();

        // 2. Make the synchronous gRPC call
        InferenceReply reply = huggingFaceStub.generateResponse(request);

        // 3. Extract and return just the string result
        return reply.getResult();
    }

}
