package com.OpenLeaf.service;

import com.OpenLeaf.modal.NotificationSettings;
import com.OpenLeaf.modal.User;
import com.OpenLeaf.payload.dto.NotificationSettingsDTO;
import com.OpenLeaf.payload.request.UpdateNotificationSettingsRequest;

/**
 * Service interface for managing notification settings
 */
public interface NotificationSettingsService {

    /**
     * Get notification settings for a user
     * @param user The user
     * @return NotificationSettings or null if not found
     */
    NotificationSettingsDTO getSettings(User user);

    /**
     * Get notification settings for a user, creating default settings if they don't exist
     * @param user The user
     * @return NotificationSettings
     */
    NotificationSettings getOrCreateSettings(User user);

    /**
     * Update notification settings for a user
     * @param user The user
     * @param request The update request
     * @return Updated NotificationSettings
     */
    NotificationSettingsDTO updateSettings(User user, UpdateNotificationSettingsRequest request);

    /**
     * Create default notification settings for a user
     * @param user The user
     * @return Created NotificationSettings
     */
    NotificationSettings createDefaultSettings(User user);
}
