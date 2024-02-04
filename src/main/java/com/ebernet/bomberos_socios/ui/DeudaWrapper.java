package com.ebernet.bomberos_socios.ui;

import com.ebernet.bomberos_socios.model.Deuda;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeudaWrapper {

    private final Deuda deuda;

    public DeudaWrapper(Deuda deuda) {
        this.deuda = deuda;
    }

    public StringProperty idDeudaProperty() {
        return new SimpleStringProperty(deuda.getIdDeuda() != null ? deuda.getIdDeuda().toString() : "");
    }

    public StringProperty cuotaProperty() {
        return new SimpleStringProperty(Integer.toString(deuda.getCuota()));
    }

    public StringProperty anioProperty() {
        return new SimpleStringProperty(Integer.toString(deuda.getAnio()));
    }

    public StringProperty importeProperty() {
        return new SimpleStringProperty(Integer.toString(deuda.getImporte()));
    }

    public StringProperty fechaGeneracionProperty() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = deuda.getFechaGeneracion() != null
                ? deuda.getFechaGeneracion().format(formatter) : "";
        return new SimpleStringProperty(fechaFormateada);
    }

    public StringProperty socioDeudorProperty() {
        return new SimpleStringProperty(deuda.getSocioDeudor() != null
                ? deuda.getSocioDeudor().getNroSocio() + " - " + deuda.getSocioDeudor().getNombreCompleto()
                : ""
        );
    }

    public StringProperty pagaProperty() {
        return new SimpleStringProperty(deuda.isPaga() ? "Si" : "No");
    }
}
