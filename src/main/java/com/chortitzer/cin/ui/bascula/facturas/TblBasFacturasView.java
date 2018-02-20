package com.chortitzer.cin.ui.bascula.facturas;

import com.chortitzer.cin.model.bascula.TblBasContratos;
import com.chortitzer.cin.model.bascula.TblBasFacturas;
import com.chortitzer.cin.model.bascula.Tblempresa;
import com.chortitzer.cin.model.bascula.Tblproductos;
import com.chortitzer.cin.ui.AbstractView;
import com.chortitzer.cin.ui.fieldextensions.*;
import com.chortitzer.cin.utils.tiwulfx.TypeAheadField;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;
import jidefx.scene.control.field.NumberField;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;

public class TblBasFacturasView extends AbstractView<TblBasFacturas> implements FxmlView<TblBasFacturasViewModel> {

    private DatePickerField dtpFecha = new DatePickerField();
    private FacturaNroField txtNro = new FacturaNroField();
    private MaskField txtNroTimbrado = new MaskField();
    private TextField txtRazonSocial = new TextField();
    private TextField txtRuc = new TextField();
    private TextFieldLong txtCantidad= new TextFieldLong();
    private Button btnRemision = new Button("Nota de Remision");

    @InjectViewModel
    private TblBasFacturasViewModel viewModel;

    public void initialize() {

        //empresaColumn.setCellFactory(TypeAheadTableCell::new);

        setViewModel(viewModel);
        initializeAbstract();

        TableColumnLocalDateTime<TblBasFacturas> col1 = new TableColumnLocalDateTime<>("Fecha", "fecha", 170.0);
        TableColumnString<TblBasFacturas> col2 = new TableColumnString<>("Nro. Factura", "nro", 150.0);
        TableColumnString<TblBasFacturas> col3 = new TableColumnString<>("Nro Timbrado", "nroTimbrado", 150.0);
        TableColumnString<TblBasFacturas> col4 = new TableColumnString<>("Razon Social", "razonSocial", 250.0);
        TableColumnString<TblBasFacturas> col5 = new TableColumnString<>("RUC", "ruc", 150.0);
        TableColumnLong<TblBasFacturas> col6 = new TableColumnLong<>("Cantidad Facturada", "cantidad", 150.0);

        itemsTable.getColumns().addAll(col1, col2, col3, col4, col5, col6);

        gridPane.add(new Label("Nro. Factura"), 1, 1);
        gridPane.add(new Label("Nro. Timbrado"), 1, 2);
        gridPane.add(new Label("Fecha"), 1, 3);
        gridPane.add(new Label("Proveedor"), 1, 4);
        gridPane.add(new Label("Cantidad Facturada"), 1, 5);

        txtNro.setMaxWidth(150);
        gridPane.add(txtNro, 2, 1);
        txtNroTimbrado.setPrefWidth(100);
        txtNroTimbrado.setMask("DDDDDDD");
        gridPane.add(txtNroTimbrado, 2, 2);
        gridPane.add(dtpFecha, 2, 3);
        txtRuc.setPrefWidth(120);
        txtRuc.setPromptText("RUC");
        txtRuc.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) {
                txtRazonSocial.setText(viewModel.getContribuyenteRazonSocial(txtRuc.getText()));
            }
        });
        txtRazonSocial.setPrefWidth(350);
        txtRazonSocial.setPromptText("Razon Social");
        HBox hBoxEmisor = new HBox();
        hBoxEmisor.setSpacing(5.0);
        hBoxEmisor.getChildren().addAll(txtRuc, txtRazonSocial);
        gridPane.add(hBoxEmisor, 2, 4);
        gridPane.add(txtCantidad, 2, 5);
        gridPane.add(btnRemision, 2, 6);

        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(facturas -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (facturas.getRazonSocial().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (facturas.getRuc().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (facturas.getNroTimbrado().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (facturas.getNro().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        itemsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, o, n) -> {
            dtpFecha.dateTimeValueProperty().bindBidirectional(viewModel.fechaProperty());
            txtNro.textProperty().bindBidirectional(viewModel.nroProperty());
            txtNroTimbrado.textProperty().bindBidirectional(viewModel.nroTimbradoProperty());
            txtRazonSocial.textProperty().bindBidirectional(viewModel.razonSocialProperty());
            txtRuc.textProperty().bindBidirectional(viewModel.rucProperty());
            txtCantidad.textProperty().bindBidirectional(viewModel.cantidadProperty(), new NumberStringConverter());
        });

        dtpFecha.dateTimeValueProperty().bindBidirectional(viewModel.fechaProperty());
        txtNro.textProperty().bindBidirectional(viewModel.nroProperty());
        txtNroTimbrado.textProperty().bindBidirectional(viewModel.nroTimbradoProperty());
        txtRazonSocial.textProperty().bindBidirectional(viewModel.razonSocialProperty());
        txtRuc.textProperty().bindBidirectional(viewModel.rucProperty());
        txtCantidad.textProperty().bindBidirectional(viewModel.cantidadProperty(), new NumberStringConverter());

        btnAdd.setOnAction((event) -> {
            addAbstract();
            viewModel.add(new TblBasFacturas());
            txtNro.requestFocus();
        });

        btnRemision.setOnAction((event) -> {
            viewModel.showTblBasNotasDeRemisionView();
        });

    }
}