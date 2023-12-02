package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;

public class InvoiceDetailController {
    private final Invoice invoice;
    private final InvoicesService invoicesService = new InvoicesService();

    public InvoiceDetailController(int invoiceId) {
        this.invoice = invoicesService.geInvoiceDetailsById(String.valueOf(invoiceId));
        System.out.println(this.invoice);
    }
}
