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

import com.noahhusby.ticketflow.entities.Ticket;
import com.noahhusby.ticketflow.entities.User;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A handler for managing tickets.
 *
 * @author Noah Husby
 */
public class TicketHandler {

    private static final TicketHandler instance = new TicketHandler();
    private Map<Integer, Ticket> ticketCache = new TreeMap<>();

    protected TicketHandler() {
    }

    public static TicketHandler getInstance() {
        return instance;
    }

    public void writeToCache(List<Ticket> ticketList) {
        Map<Integer, Ticket> temp = new TreeMap<>();
        ticketList.forEach(ticket -> temp.put(ticket.getId(), ticket));
        this.ticketCache = temp;
    }

    public Map<Integer, Ticket> getTickets() {
        return ticketCache;
    }

    /**
     * Creates a new ticket and saves it to the database / cache.
     *
     * @param user        The user who issued the ticket.
     * @param description The description of the ticket.
     * @return {@link Ticket}.
     */
    public Ticket createNewTicket(User user, String description) {
        Ticket ticket = Dao.getInstance().saveNewTicket(user, description);
        ticketCache.put(ticket.getId(), ticket);
        return ticket;
    }

    /**
     * Removes a ticket based upon a given ticket object.
     *
     * @param user   who initiated the request.
     * @param ticket The ticket to be deleted.
     */
    public void removeTicket(User user, Ticket ticket) {
        Dao.getInstance().removeTicket(user, ticket.getId());
        this.ticketCache.remove(ticket.getId());
    }
}
