import { createContext, useContext, useState, useEffect } from "react";
import api from "../services/api";
import { isLoggedIn, getEmail } from "../services/auth";

const CartContext = createContext();

/** Hook so components can read the cart count without prop-drilling. */
export function useCart() {
    return useContext(CartContext);
}

export function CartProvider({ children }) {

    const [cartCount, setCartCount] = useState(0);

    /** Re-fetch the cart and recalculate the total item count. */
    const refreshCart = async () => {

        if (!isLoggedIn()) {
            setCartCount(0);
            return;
        }

        try {

            const response = await api.get("/cart", {
                params: { email: getEmail() }
            });

            // Sum quantities, not just number of rows —
            // 3 of one item should count as 3.
            const total = response.data.reduce(
                (sum, item) => sum + (item.quantity || 0),
                0
            );

            console.log("refreshCart -> total:", total, response.data);
            setCartCount(total);

        } catch (error) {
            console.log("refreshCart FAILED:", error);
            setCartCount(0);
        }

    };

    // Load once when the app mounts.
    useEffect(() => {
        refreshCart();
    }, []);

    return (
        <CartContext.Provider value={{ cartCount, refreshCart }}>
            {children}
        </CartContext.Provider>
    );
}