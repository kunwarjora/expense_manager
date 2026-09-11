package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialAdvisorService {
    private final ChatClient chatClient;
    private final RecurringExpenseRepository repository;


    public FinancialAdvisorService(ChatClient.Builder chatClientBuilder, RecurringExpenseRepository repository) {
        this.repository = repository;
        this.chatClient = chatClientBuilder.defaultSystem("""
                    You are a strict, witty, and highly intelligent Indian financial advisor.
                                                        Your goal is to ruthlessly analyze the user's monthly subscriptions and expose their opportunity cost.
                
                                                        Instructions:
                                                        1. Identify redundancies (e.g., multiple streaming services) or "useless" expenses.
                                                        2. Calculate the total monthly cost of these "wasted" expenses.
                                                        3. Tell the user exactly how much wealth they would generate if they cancelled those subscriptions and invested that amount into a Nifty 50 Mutual Fund SIP for 10 years at a 12% annual return.
                                                        4. Tell the user how many grams of 24K Gold they could buy in a year with that wasted money (Assume Gold is roughly ₹7,500 per gram).
                                                        5. Keep the tone witty and slightly harsh. Use Indian Rupees (₹).
                                                        6. Output the response in clean, readable Markdown format.
                """).build();
    }

    public String generateRoast(String userId){
        List<Expenses> expenses = repository.findByUserIdOrderByNextBillingDateAsc(userId);
        if (expenses.isEmpty()) {
            return "You have no active subscriptions. Good job, you are already saving money!";
        }

        String expenseData= expenses.stream()
                .map(exp-> exp.getTitle() +": ₹"+ exp.getAmount()+" ("+ exp.getBillingCycle()+")")
                .collect(Collectors.joining("\n"));

        String userMessage = "Here are my current Active Subscriptions:\n"+expenseData;

        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    public String health() {
        return chatClient.prompt()
                .user("Hello, Good Morning")
                .call()
                .content();
    }
}

