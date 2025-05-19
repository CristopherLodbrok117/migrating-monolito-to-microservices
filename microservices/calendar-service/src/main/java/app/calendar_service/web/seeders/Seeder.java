package app.calendar_service.web.seeders;

public interface Seeder {
    void seed();
    String getName();
    Integer getOrder();
}
