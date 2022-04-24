/*
 * TicketFlow Copyright (C) 2022 Noah Husby
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahhusby.ticketflow;

import com.noahhusby.ticketflow.entities.History;
import com.noahhusby.ticketflow.entities.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A handler for managing events.
 *
 * @author Noah Husby
 */
public class HistoryHandler {

    private static final HistoryHandler instance = new HistoryHandler();
    private Map<LocalDateTime, History> historyByTimestamp = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, Map<LocalDateTime, History>> historyByUser = new HashMap<>();

    protected HistoryHandler() {
    }

    public static HistoryHandler getInstance() {
        return instance;
    }

    public void writeToCache(List<History> historyList) {
        Map<LocalDateTime, History> _historyByTimestamp = new TreeMap<>();
        Map<Integer, Map<LocalDateTime, History>> _historyByUser = new HashMap<>();
        for (History history : historyList) {
            _historyByTimestamp.put(history.getTimestamp(), history);
            _historyByUser.putIfAbsent(history.getUser(), new TreeMap<>(Collections.reverseOrder()));
            _historyByUser.get(history.getUser()).put(history.getTimestamp(), history);
        }
        this.historyByTimestamp = _historyByTimestamp;
        this.historyByUser = _historyByUser;
    }

    /**
     * Gets a list of all historical events.
     *
     * @return A map of historical events sorted by reverse chronological order.
     */
    public Map<LocalDateTime, History> getHistory() {
        return historyByTimestamp;
    }

    /**
     * Gets a list of all historical events for a given user.
     *
     * @param user {@link User}.
     * @return A map of historical events of the specified user sorted by reverse chronological order.
     */
    public Map<LocalDateTime, History> getHistoryByUser(User user) {
        return getHistoryByUser(user.getId());
    }

    /**
     * Gets a list of all historical events for a given user.
     *
     * @param id of user.
     * @return A map of historical events of the specified user sorted by reverse chronological order.
     */
    public Map<LocalDateTime, History> getHistoryByUser(Integer id) {
        return historyByUser.get(id);
    }

    /**
     * Gets whether a given user has any history written.
     *
     * @param user The user to check.
     * @return True if user has history, false otherwise.
     */
    public boolean doesUserHaveHistory(User user) {
        return historyByUser.containsKey(user.getId());
    }
}
