package com.stocktrace.stocktrace.config;

import com.stocktrace.stocktrace.entity.Category;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.entity.Role;
import com.stocktrace.stocktrace.entity.User;
import com.stocktrace.stocktrace.repository.CategoryRepository;
import com.stocktrace.stocktrace.repository.ProductRepository;
import com.stocktrace.stocktrace.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createDefaultUsers();
        importExcelDataIfEmpty();
    }


    private void createDefaultUsers() {
        createUserIfMissing(
                "Admin",
                "admin@stocktrace.com",
                "admin123",
                Role.ADMIN
        );

        createUserIfMissing(
                "Magasinier",
                "magasinier@stocktrace.com",
                "magasinier123",
                Role.MAGASINIER
        );
    }

    private void createUserIfMissing(
            String name,
            String email,
            String password,
            Role role) {

        // We check by ROLE, not by email.
        // This prevents creating a second account
        // if the existing shared account changes its email.
        if (!userRepository.existsByRole(role)) {

            User user = new User(
                    name,
                    email,
                    passwordEncoder.encode(password),
                    role
            );

            userRepository.save(user);

            System.out.println(
                    ">>> Created default " + role + ": " + email
            );
        }
    }
    private void importExcelDataIfEmpty() {
        if (productRepository.count() > 0) {
            System.out.println(">>> Product data already exists; Excel seed skipped.");
            return;
        }

        InputStream stream = getClass().getResourceAsStream("/data/stock_data.csv");
        if (stream == null) {
            System.out.println(">>> stock_data.csv not found; no product seed imported.");
            return;
        }

        int imported = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                List<String> c = parseCsvLine(line);
                if (c.size() < 9) continue;

                String barcode = clean(c.get(1));
                String reference = clean(c.get(2));
                Integer quantity = integerValue(c.get(3));
                String affectation = clean(c.get(4));
                String type = clean(c.get(5));
                String mark = clean(c.get(6));
                String remarks = clean(c.get(7));
                String tag = clean(c.get(8));

                if (reference == null && barcode == null) continue;

                Category category = null;
                if (type != null) {
                    category = categoryRepository.findByNameIgnoreCase(type)
                            .orElseGet(() -> categoryRepository.save(new Category(type, "Imported from Types")));
                }

                Product product = new Product();
                product.setReference(reference);
                product.setBarcode(barcode);
                product.setQuantity(quantity);
                product.setAffectation(affectation);
                product.setType(type);
                product.setMark(mark);
                product.setRemarks(remarks);
                product.setTag(tag);
                product.setMinStockAlert(5);
                product.setCategory(category);

                productRepository.save(product);
                imported++;
            }

            System.out.println(">>> Imported " + imported + " products from stock_data.csv");
        } catch (Exception e) {
            throw new IllegalStateException("Could not import stock_data.csv", e);
        }
    }

    private static String clean(String value) {
        if (value == null) return null;
        String s = value.trim();
        return s.isEmpty() || s.equalsIgnoreCase("nan") ? null : s;
    }

    private static Integer integerValue(String value) {
        String s = clean(value);
        if (s == null) return 0;
        try {
            return (int) Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (ch == ',' && !quoted) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        result.add(current.toString());
        return result;
    }
}
