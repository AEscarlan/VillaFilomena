package com.example.villafilomena.Frontdesk;

public class Guest_details_model {
    int list_no;
    String booking_id, currentBooking_Date, guest_name, checkIn_date, checkIn_time, checkOut_date, checkOut_time,
            guest_count, room_id, cottage_id, total_cost, pay, payment_status, balance, reference_num, booking_status, invoice;

    public Guest_details_model(int list_no, String booking_id, String currentBooking_Date, String guest_name, String checkIn_date, String checkIn_time, String checkOut_date, String checkOut_time, String guest_count, String room_id, String cottage_id, String total_cost, String pay, String payment_status, String balance, String reference_num, String booking_status, String invoice) {
        this.list_no = list_no;
        this.booking_id = booking_id;
        this.currentBooking_Date = currentBooking_Date;
        this.guest_name = guest_name;
        this.checkIn_date = checkIn_date;
        this.checkIn_time = checkIn_time;
        this.checkOut_date = checkOut_date;
        this.checkOut_time = checkOut_time;
        this.guest_count = guest_count;
        this.room_id = room_id;
        this.cottage_id = cottage_id;
        this.total_cost = total_cost;
        this.pay = pay;
        this.payment_status = payment_status;
        this.balance = balance;
        this.reference_num = reference_num;
        this.booking_status = booking_status;
        this.invoice = invoice;
    }

    public int getList_no() {
        return list_no;
    }

    public void setList_no(int list_no) {
        this.list_no = list_no;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getCurrentBooking_Date() {
        return currentBooking_Date;
    }

    public void setCurrentBooking_Date(String currentBooking_Date) {
        this.currentBooking_Date = currentBooking_Date;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getCheckIn_date() {
        return checkIn_date;
    }

    public void setCheckIn_date(String checkIn_date) {
        this.checkIn_date = checkIn_date;
    }

    public String getCheckIn_time() {
        return checkIn_time;
    }

    public void setCheckIn_time(String checkIn_time) {
        this.checkIn_time = checkIn_time;
    }

    public String getCheckOut_date() {
        return checkOut_date;
    }

    public void setCheckOut_date(String checkOut_date) {
        this.checkOut_date = checkOut_date;
    }

    public String getCheckOut_time() {
        return checkOut_time;
    }

    public void setCheckOut_time(String checkOut_time) {
        this.checkOut_time = checkOut_time;
    }

    public String getGuest_count() {
        return guest_count;
    }

    public void setGuest_count(String guest_count) {
        this.guest_count = guest_count;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getCottage_id() {
        return cottage_id;
    }

    public void setCottage_id(String cottage_id) {
        this.cottage_id = cottage_id;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getReference_num() {
        return reference_num;
    }

    public void setReference_num(String reference_num) {
        this.reference_num = reference_num;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
