package com.ebernet.bomberos_socios.ui;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SocioAdherenteWrapper {

    private final SocioAdherente socio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SocioAdherenteWrapper(SocioAdherente socio) {
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

    public StringProperty fechaBajaProperty() {
        String fechaFormateada = socio.getFechaBaja() != null ? socio.getFechaBaja().format(formatter) : "";
        return new SimpleStringProperty(fechaFormateada);
    }

    public StringProperty bajaProperty() {
        return new SimpleStringProperty(socio.isBaja() ? "Si" : "No");
    }

    public StringProperty tipoBajaProperty() {
        return new SimpleStringProperty(socio.getTipoBaja() != null ? socio.getTipoBaja().getNombre() : "");
    }

    public StringProperty socioTitularProperty() {
        return new SimpleStringProperty(socio.getSocioTitular() != null ? socio.getSocioTitular().getNroSocio() + " - " + socio.getSocioTitular().getNombreCompleto() : "");
    }

    public StringProperty vinculoProperty() {
        return new SimpleStringProperty(socio.getVinculo() != null ? socio.getVinculo().getNombre() : "");
    }
}
