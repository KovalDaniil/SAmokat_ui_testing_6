package test.data;

/* Данные для параметризации теста заказа самоката. */
public class OrderData {
    public String firstName;
    public String lastName;
    public String address;
    public String phone;
    public String date;
    public String period;
    public String color;
    public String comment;

    public OrderData(String firstName, String lastName, String address, String phone,
                     String date, String period, String color, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.color = color;
        this.comment = comment;
    }
}

