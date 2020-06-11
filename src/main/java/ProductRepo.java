import java.util.*;
import java.util.stream.Collectors;

public class ProductRepo {

    private static ProductRepo instance = null;
    private Map<String, Product> products = null;

    private ProductRepo() {
        products = new HashMap<String, Product>();
    }

    public static ProductRepo getInstance() {
        if (instance == null ) {
            instance = new ProductRepo();
        }

        return instance;
    }

    public void addProduct(Product product) {
        product.setCreatedDate(new Date());
        products.putIfAbsent(product.getId(), product);
    }

    public void updateProduct(Product product) {
        Product originalProduct = products.get(product.getId());

        if ( product.getName() != null ) {
            originalProduct.setName(product.getName());
        }
        if ( product.getPrice() > 0.0 ) {
            originalProduct.setPrice(product.getPrice());
        }
        if (product.getCategory() != null ){
            originalProduct.setCategory(product.getCategory());
        }
        originalProduct.setUpdatedDate(new Date());
    }

    public void deleteProduct(Product product) {
        products.remove(product.getId());
    }

    public void deleteProductById(String id) {
        products.remove(id);
    }

    public Product getProductById(String id) {
        return products.get(id);
    }

    public List<Product> getProductByName(String name) {
        Map<String, Product> map = products.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getName().equalsIgnoreCase(name))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new ArrayList<>(map.values());
    }

    public List<Product> getAll() {
        return new ArrayList<>(products.values());
    }
}
