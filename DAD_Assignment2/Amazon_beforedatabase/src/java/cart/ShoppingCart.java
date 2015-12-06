package cart;

import java.util.*;
import database.BookDetails;

public class ShoppingCart {

    private HashMap items = null;

    public ShoppingCart() {
        items = new HashMap();
    }

    public synchronized void add(String bookId, BookDetails book) {
        if (items.containsKey(bookId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(bookId);
            scitem.incrementQuantity();
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(book);
            items.put(bookId, newItem);
        }
    }

    public synchronized void remove(String bookId) {
        if (items.containsKey(bookId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(bookId);
            scitem.decrementQuantity();
            if (scitem.getQuantity() <= 0) {
                items.remove(bookId);
            }
        }
    }

    public synchronized List getItems() {
        List results = new ArrayList();
        Iterator i = items.values().iterator();
        while (i.hasNext()) {
            results.add(i.next());
        }
        return (results);
    }

    protected void finalize() throws Throwable {
        items.clear();
    }

    public synchronized int getNumberOfItems() {
        int numberOfItems = 0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            numberOfItems += item.getQuantity();
        }
        return numberOfItems;
    }

    public synchronized double getTotal() {
        double amount = 0.0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            BookDetails bookDetails = (BookDetails) item.getItem();
            amount += (item.getQuantity() * bookDetails.getPrice());
        }
        return roundOff(amount);
    }

    static private double roundOff(double x) {
        return Math.round(x * 100) / 100.0;
    }

    public synchronized void clear() {
        items.clear();
    }
}
