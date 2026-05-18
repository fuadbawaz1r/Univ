package com.lowheatpizza.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.managers.GameConfigManager.CustomerConfig;
import com.lowheatpizza.managers.GameConfigManager.OrderConfig;
import com.lowheatpizza.models.Customer;
import com.lowheatpizza.models.Order;
import com.lowheatpizza.models.Player;

public class CustomerManager {
    private final GameConfigManager configManager;

    public CustomerManager(GameConfigManager configManager) {
        this.configManager = configManager;
    }

    public Customer nextCustomer(Player player, ToppingManager toppingManager) {
        Array<CustomerCandidate> candidates = buildCandidates(player, toppingManager, false);
        if (candidates.size == 0) {
            candidates = buildCandidates(player, toppingManager, true);
        }
        if (candidates.size == 0) {
            throw new IllegalStateException("No valid customer order available for owned toppings.");
        }

        CustomerCandidate selected = candidates.random();
        Order order = new Order(
            selected.orderConfig.id,
            selected.orderConfig.availableFromLevel,
            selected.orderConfig.dialogue,
            selected.orderConfig.clarificationDialogue,
            selected.orderConfig.requiredToppings,
            selected.orderConfig.reward.money,
            selected.orderConfig.reward.exp
        );
        return new Customer(
            selected.customerConfig.id,
            selected.customerConfig.displayName,
            selected.customerConfig.texturePath,
            selected.customerConfig.availableFromLevel,
            order,
            selected.orderConfig.dialogue,
            selected.orderConfig.clarificationDialogue
        );
    }

    private Array<CustomerCandidate> buildCandidates(Player player, ToppingManager toppingManager, boolean basicOnly) {
        Array<CustomerCandidate> candidates = new Array<>();
        Array<String> ownedToppings = toppingManager.getOwnedToppingIds();

        for (CustomerConfig customerConfig : configManager.getCustomers()) {
            if (customerConfig.availableFromLevel > player.getLevel()) {
                continue;
            }
            for (OrderConfig orderConfig : customerConfig.orders) {
                if (orderConfig.availableFromLevel > player.getLevel()) {
                    continue;
                }
                if (!allToppingsOwned(orderConfig.requiredToppings, ownedToppings)) {
                    continue;
                }
                if (basicOnly && orderConfig.availableFromLevel > 1) {
                    continue;
                }
                candidates.add(new CustomerCandidate(customerConfig, orderConfig));
            }
        }
        return candidates;
    }

    private boolean allToppingsOwned(Array<String> requiredToppings, Array<String> ownedToppings) {
        for (String toppingId : requiredToppings) {
            if (!ownedToppings.contains(toppingId, false)) {
                return false;
            }
        }
        return true;
    }

    private static class CustomerCandidate {
        private final CustomerConfig customerConfig;
        private final OrderConfig orderConfig;

        private CustomerCandidate(CustomerConfig customerConfig, OrderConfig orderConfig) {
            this.customerConfig = customerConfig;
            this.orderConfig = orderConfig;
        }
    }
}

