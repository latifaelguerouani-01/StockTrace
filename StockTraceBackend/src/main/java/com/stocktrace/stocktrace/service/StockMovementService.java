package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.entity.MovementType;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.entity.StockMovement;
import com.stocktrace.stocktrace.entity.User;
import com.stocktrace.stocktrace.exception.BadRequestException;
import com.stocktrace.stocktrace.exception.ResourceNotFoundException;
import com.stocktrace.stocktrace.repository.ProductRepository;
import com.stocktrace.stocktrace.repository.StockMovementRepository;
import com.stocktrace.stocktrace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<StockMovement> getMovementsByProductId(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public StockMovement getMovementById(Long id) {
        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
    }

    @Transactional
    public StockMovement createMovement(StockMovement movement, Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        movement.setProduct(product);
        movement.setUser(user);

        // Modyfi l-quantité dyal l-product 3la ḥsab l-type Enum (IN / OUT)
        if (movement.getType() == MovementType.IN) {
            product.setQuantity(product.getQuantity() + movement.getQuantity());
        } else if (movement.getType() == MovementType.OUT) {
            if (product.getQuantity() < movement.getQuantity()) {
                throw new BadRequestException("Stock insuffisant pour la sortie !");
            }
            product.setQuantity(product.getQuantity() - movement.getQuantity());
        }

        productRepository.save(product);
        return stockMovementRepository.save(movement);
    }

    @Transactional
    public StockMovement recordMovement(Long productId, Integer quantity, MovementType type, String reason) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (type == MovementType.OUT && product.getQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock. Current stock: " + product.getQuantity());
        }

        if (type == MovementType.IN) {
            product.setQuantity(product.getQuantity() + quantity);
        } else if (type == MovementType.OUT) {
            product.setQuantity(product.getQuantity() - quantity);
        }

        productRepository.save(product);

        StockMovement movement = new StockMovement(product, quantity, type, reason);
        return stockMovementRepository.save(movement);
    }
}