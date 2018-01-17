package com.chortitzer.cin.bas.precioscontratos.ui.bascula.pesadas;

import com.chortitzer.cin.bas.precioscontratos.model.bascula.TblBasNotasDeRemision;
import com.chortitzer.cin.bas.precioscontratos.model.bascula.Tblpesadas;
import com.chortitzer.cin.bas.precioscontratos.model.bascula.Tblempresa;
import com.chortitzer.cin.bas.precioscontratos.model.bascula.Tblproductos;
import com.chortitzer.cin.bas.precioscontratos.model.dao.bascula.TblpesadasDao;
import com.chortitzer.cin.bas.precioscontratos.model.dao.bascula.TblempresaDao;
import com.chortitzer.cin.bas.precioscontratos.model.dao.bascula.TblproductosDao;
import com.chortitzer.cin.bas.precioscontratos.ui.AbstractViewModel;
import com.chortitzer.cin.bas.precioscontratos.ui.bascula.notasderemision.TblBasNotasDeRemisionView;
import com.chortitzer.cin.bas.precioscontratos.ui.bascula.notasderemision.TblBasNotasDeRemisionViewModel;
import com.chortitzer.cin.bas.precioscontratos.ui.bascula.tblbascontratos.TblBasContratosView;
import com.chortitzer.cin.bas.precioscontratos.ui.bascula.tblbascontratos.TblBasContratosViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class TblpesadasViewModel extends AbstractViewModel<Tblpesadas> {

    @Inject
    TblempresaDao tblempresaDao;
    private final ObservableList<Tblempresa> empresasList = FXCollections.observableArrayList();

    @Inject
    TblproductosDao tblproductosDao;
    private final ObservableList<Tblproductos> productosList = FXCollections.observableArrayList();

    @Inject
    TblpesadasDao tblpesadasDao;

    @Inject
    Stage primayStage;

    public void initialize() {
        setDao(tblpesadasDao);
        initializeAbstract();
        empresasList.addAll(tblempresaDao.findAll());
        productosList.addAll(tblproductosDao.findAll());
    }

    public ObjectProperty<LocalDateTime> fechaProperty() {
        return itemWrapper.field("fecha", Tblpesadas::getFechahora, Tblpesadas::setFechahora);
    }

    public StringProperty idRemisionProperty() {
        return itemWrapper.field("idRemision", Tblpesadas::getIdRemision, Tblpesadas::setIdRemision);
    }

    public StringProperty chapaProperty() {
        return itemWrapper.field("chapa", Tblpesadas::getChapa, Tblpesadas::setChapa);
    }

    public ObjectProperty<Tblempresa> empresaProperty() {
        return itemWrapper.field("empresa", Tblpesadas::getEmpresaid, Tblpesadas::setEmpresaid);
    }

    public ObjectProperty<Tblproductos> productoProperty() {
        return itemWrapper.field("producto", Tblpesadas::getProductoid, Tblpesadas::setProductoid);
    }

    public IntegerProperty brutoProperty() {
        return itemWrapper.field("bruto", Tblpesadas::getBruto, Tblpesadas::setBruto);
    }

    public IntegerProperty taraProperty() {
        return itemWrapper.field("tara", Tblpesadas::getTara, Tblpesadas::setTara);
    }

    public IntegerProperty precioProperty() {
        return itemWrapper.field("precioGsPorKg", Tblpesadas::getPrecioGsPorKg, Tblpesadas::setPrecioGsPorKg);
    }

    public ObservableList<Tblempresa> getEmpresas() {
        return empresasList;
    }

    public ObservableList<Tblproductos> getProductos() {
        return productosList;
    }

    public void showTblBasNotasDeRemisionView() {
        final ViewTuple<TblBasNotasDeRemisionView, TblBasNotasDeRemisionViewModel> tuple = FluentViewLoader.fxmlView(TblBasNotasDeRemisionView.class).load();
        if (selectedItem.get().getIdNotaDeRemision() != null) {
            tuple.getViewModel().itemWrapper.set(selectedItem.get().getIdNotaDeRemision());
        } else {
            tuple.getViewModel().itemWrapper.set(new TblBasNotasDeRemision());
        }
        Stage modal = new Stage();
        modal.initOwner(primayStage);
        tuple.getCodeBehind().owningStage.set(modal);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setScene(new Scene(tuple.getView()));
        modal.showAndWait();

        if (tuple.getViewModel().commitStatus == TblBasNotasDeRemisionViewModel.CommitStatus.COMMIT) {
            selectedItem.get().setIdNotaDeRemision(tuple.getViewModel().itemWrapper.get());
        }
    }

}
