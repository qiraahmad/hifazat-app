package com.example.tracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>();
    private MutableLiveData<contact> new_contact = new MutableLiveData<>();
    private MutableLiveData<contact> edit_contact = new MutableLiveData<>();
    private MutableLiveData<contact> del_contact = new MutableLiveData<>();
    private MutableLiveData<ArrayList<contact>> edit_contacts= new MutableLiveData<>();

    public void setText(String input) { text.setValue(input); }

    public LiveData<String> getText() {
        return text;
    }

    public void setContact(contact c1) {
        new_contact.setValue(c1);
    }

    public LiveData<contact> getContact() { return new_contact; }

    public void setEditContact1(contact c1) { edit_contact.setValue(c1); }

    public LiveData<contact> getEditContact1() { return edit_contact; }

    public void setDelContact1(contact c1) { del_contact.setValue(c1); }

    public LiveData<contact> getDelContact1() { return del_contact; }

    public void setEditContact(contact c1, contact c2) {
        ArrayList<contact> temp1 = new ArrayList<>();
        temp1.add(c1);
        temp1.add(c2);
        edit_contacts.setValue(temp1);
    }
    public LiveData<ArrayList<contact>> getEditContact() { return edit_contacts; }
}

