package com.application.restaurant.configuration.scheduler;


import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.liqpay.LiqPayController;
import com.application.restaurant.model.Request;
import com.application.restaurant.model.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class PaidOverseerScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LiqPayController liqPayController;

    @Autowired
    private RequestRepository requestRepository;

    @Scheduled(fixedRate = 30_000)
    public void scheduler() {
        List<Request> acceptedRequests =  requestRepository.getAllRequests()
                .stream()
                .filter(request -> request.getRequestStatus() == RequestStatus.ACCEPTED)
                .collect(Collectors.toList());

        checkForPaidRequests(acceptedRequests);
        checkForExpiredRequests(acceptedRequests);

    }

    public void checkForExpiredRequests(List<Request> acceptedRequests){
        List<Request> expiredAcceptedRequests = acceptedRequests
                .stream()
                .filter(request -> LocalDateTime.now().isAfter(request.getStatusUpdatedAt().plusMinutes(20)))
                .collect(Collectors.toList());

        for(Request request : expiredAcceptedRequests) {
            request.setRequestStatus(RequestStatus.CANCELLED);
            request.setStatusUpdatedAt(LocalDateTime.now());
            requestRepository.saveRequest(request);
            System.out.println("Scheduler: Request with id:" + request.getId() + " was cancelled.");
        }
    }

    public void checkForPaidRequests(List<Request> acceptedRequests) {
        List<Request> validAcceptedRequests = acceptedRequests
                .stream()
                .filter(request -> request.getStatusUpdatedAt().isBefore(request.getStatusUpdatedAt().plusMinutes(20)))
                .collect(Collectors.toList());

        for(Request request : validAcceptedRequests) {
            try {
                if(liqPayController.getPaymentStatus(request.getId()).equals("success")) {
                    request.setRequestStatus(RequestStatus.PAID);
                    request.setStatusUpdatedAt(LocalDateTime.now());
                    requestRepository.saveRequest(request);
                    orderRepository.addOrder(request.getOrder());
                    System.out.println("Scheduler: Request with id:" + request.getId() + " was complete.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
