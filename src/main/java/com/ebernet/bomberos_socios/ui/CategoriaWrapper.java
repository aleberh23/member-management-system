package com.ebernet.bomberos_socios.ui;

import com.ebernet.bomberos_socios.model.Categoria;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoriaWrapper {

  private final Categoria categoria;

  public CategoriaWrapper(Categoria categoria) {
    this.categoria = categoria;
  }

  public StringProperty idCategoriaProperty() {
    return new SimpleStringProperty(categoria.getIdCategoria() != null ? categoria.getIdCategoria().toString() : "");
  }

  public StringProperty nombreProperty() {
    return new SimpleStringProperty(categoria.getNombre() != null ? categoria.getNombre() : "");
  }

  public StringProperty importeProperty() {
    return new SimpleStringProperty(Integer.toString(categoria.getImporte()));
  }

  public StringProperty cantidadSociosProperty() {
    return new SimpleStringProperty(categoria.getSocios() != null ? String.valueOf(categoria.getSocios().size()) : "");
  }

}
