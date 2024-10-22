package my.rest_api.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Spring REST API")
                .description("Spring REST API")
                .build();
        assertThat(event).isNotNull();
    }


    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "Spring REST API";
        String description = "Spring REST API";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}