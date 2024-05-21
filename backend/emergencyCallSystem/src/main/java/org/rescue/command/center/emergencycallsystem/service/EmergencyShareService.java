package org.rescue.command.center.emergencycallsystem.service;

import org.javatuples.Triplet;

import java.util.List;

public interface EmergencyShareService {
    /**
     * Shares a emergency with the given organizations
     * @param token JWT token of the requesting user
     * @param emergencyId Id of the emergency
     * @param data contains the following data: organizationName, districtName, List with ids of shared messages
     * @return successful?
     */
    boolean shareEmergency(String token, Long emergencyId, List<Triplet<String, String, List<Long>>> data);
}