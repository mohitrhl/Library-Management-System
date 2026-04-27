package com.OpenLeaf.service;

import com.OpenLeaf.payload.EmailNotificationDTO;


public interface EmailService {


    void sendEmail(String to, String subject, String body);

    void sendEmail(EmailNotificationDTO notification);

    void sendTemplatedEmail(EmailNotificationDTO notification);

    void sendOverdueReminder(String recipient, String userName, String bookTitle,
                            String dueDate, int overdueDays, String fineAmount);


    void sendDueDateReminder(String recipient, String userName, String bookTitle,
                            String dueDate, int daysUntilDue);

    void sendReservationAvailableNotification(String recipient, String userName, String bookTitle,
                                             String availableUntil, int holdPeriodHours);
}
