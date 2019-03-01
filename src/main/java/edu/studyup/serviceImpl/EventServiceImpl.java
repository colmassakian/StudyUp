package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import edu.studyup.entity.Event;
import edu.studyup.service.EventService;
import redis.clients.jedis.Jedis;

public class EventServiceImpl implements EventService {
	
	private Jedis jedis;
	private Gson gson;

	public EventServiceImpl(String URL) {
		this.jedis = new Jedis(URL, 8888);
		this.gson = new Gson();
	}
	
	@Override
<<<<<<< HEAD
	public Event updateEventName(int eventID, String name) throws StudyUpException {
		Event event = DataStorage.eventData.get(eventID);
		if(event == null) {
			throw new StudyUpException("No event found.");
		}

		if (name.length() >= 20) {
			throw new StudyUpException("Length too long. Maximun is 20");
		}
		event.setName(name);
		DataStorage.eventData.put(eventID, event);
		event = DataStorage.eventData.get(event.getEventID());
		return event;
	}

	@Override
	public List<Event> getActiveEvents() {
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> activeEvents = new ArrayList<>();
		// Bug: Use entry instead of keyset
		for (Map.Entry<Integer, Event> entry : eventData.entrySet()) {
			Event ithEvent= entry.getValue();
			activeEvents.add(ithEvent);
		}
		return activeEvents;
	}

	@Override
	public List<Event> getPastEvents() {
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> pastEvents = new ArrayList<>();
		
		// Bug: Use entry instead of keyset
		for (Map.Entry<Integer, Event> entry : eventData.entrySet()) {
			Event ithEvent= entry.getValue();
			// Checks if an event date is before today, if yes, then add to the past event list.
			if(ithEvent.getDate().before(new Date())) {
				pastEvents.add(ithEvent);
			}
		}
		return pastEvents;
=======
	public Event getEvent(String eventID) {
		String eventString = jedis.get(eventID);
		return gson.fromJson(eventString, Event.class);
	}
	
	@Override
	public void createEvent(String eventID, Event event) {
		String eventString = gson.toJson(event);
		jedis.set(eventID, eventString);
	}

	@Override
	public void updateEvent(String eventID, Event event) {
		String eventString = gson.toJson(event);
		jedis.set(eventID, eventString);
	}

	@Override
	public long deleteEvent(String key) {
		return jedis.del(String.valueOf(key));
>>>>>>> 17d4b9220e2108388a02b374d2bd35f93c3ad6bf
	}

	@Override
	public List<Event> getAllEvents() {
		List<Event> eventList = new ArrayList<>();
		//ToDo: No the optimized way.
		Set<String> keys = jedis.keys("*");
		for (String key: keys) {
			Event event = getEvent(key);
			eventList.add(event);
		}
		return eventList;
	}

	@Override
	public String deleteAll() {
		return jedis.flushAll();
	}
}
