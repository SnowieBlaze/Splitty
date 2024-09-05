package server.database;


import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Find an event by its invite code
     * @param inviteCode - the invite code of the event
     * @return the event with the invite code
     */
    Optional<Event> findByInviteCode(String inviteCode);
}