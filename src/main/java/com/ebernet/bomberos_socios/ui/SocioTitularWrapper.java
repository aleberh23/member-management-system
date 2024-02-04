package com.ebernet.bomberos_socios.ui;

import com.ebernet.bomberos_socios.model.SocioTitular;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SocioTitularWrapper {

    private final SocioTitular socio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SocioTitularWrapper(SocioTitular socio) {
        this.socio = socio;
    }

    public StringProperty nroSocioProperty() {
        return new SimpleStringProperty(socio.getNroSocio() != null ? socio.getNroSocio().toString() : "");
    }

    public StringProperty nombreCompletoProperty() {
        return new SimpleStringProperty(socio.getNombreCompleto() != null ? socio.getNombreCompleto() : "");
    }

    public StringProperty tipoDocProperty() {
        return new SimpleStringProperty(socio.getTipoDoc() != null ? socio.getTipoDoc().getNombre() : "");
    }

    public StringProperty nroDocumentoProperty() {
        return new SimpleStringProperty(socio.getNroDocumento() != null ? socio.getNroDocumento().toString() : "");
    }

    public StringProperty fechaNacimientoProperty() {
        String fechaFormateada = (socio.getFechaNacimiento() != null) ? socio.getFechaNacimiento().format(formatter) : "";
        return new SimpleStringProperty(fechaFormateada);
    }

    public StringProperty fechaIngresoProperty() {
        String fechaFormateada = (socio.getFechaIngreso() != null) ? socio.getFechaIngreso().format(formatter) : "";
        return new SimpleStringProperty(fechaFormateada);
    }

    public StringProperty fechaBajaProperty() {
        if (socio.getFechaBaja() != null) {
            String fechaFormateada = socio.getFechaBaja().format(formatter);
            return new SimpleStringProperty(fechaFormateada);
        } else {
            return new SimpleStringProperty("");
        }
    }

    public StringProperty bajaProperty() {
        return new SimpleStringProperty(socio.isBaja() ? "Si" : "No");
    }

    public StringProperty domicilioProperty() {
        return new SimpleStringProperty(
                (socio.getDomicilio() != null) ? socio.getDomicilio().getCalle() + " " + socio.getDomicilio().getNro() : ""
        );
    }

    public StringProperty categoriaProperty() {
        return new SimpleStringProperty(socio.getCategoria() != null ? socio.getCategoria().getNombre() : "");
    }

    public StringProperty localidadProperty() {
        return new SimpleStringProperty(
                (socio.getDomicilio() != null && socio.getDomicilio().getLocalidad() != null) ?
                        socio.getDomicilio().getLocalidad().getNombre() : ""
        );
    }
}
