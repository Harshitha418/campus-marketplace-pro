import { FaHeart, FaShoppingCart, FaEye, FaTag } from "react-icons/fa";
import api from "../services/api";
import { getEmail } from "../services/auth";
import { useNavigate } from "react-router-dom";
function ProductCard({ product }) {

    const navigate = useNavigate();
    const addToWishlist = async () => {

        try {

            const email = getEmail();

            await api.post(
                `/wishlist/${product.id}`,
                null,
                {
                    params: {
                        email: email
                    }
                }
            );

            alert("❤️ Added to Wishlist!");

        } catch (error) {

            console.log(error);

            alert("Failed to add to Wishlist");

        }

    };

    const addToCart = async () => {

        try {

            const email = getEmail();

            await api.post(
                `/cart/${product.id}`,
                null,
                {
                    params: {
                        email: email
                    }
                }
            );

            alert("🛒 Added to Cart!");

        } catch (error) {

            console.log(error);

            alert("Failed to add to Cart");

        }

    };

    return (

        <div className="card shadow-lg border-0 h-100 rounded-4">

            <div className="card-body d-flex flex-column">

                <div
                    className="text-center mb-3"
                    style={{ fontSize: "60px" }}
                >
                    📦
                </div>

                <h5 className="fw-bold">
                    {product.title}
                </h5>

                <p
                    className="text-muted"
                    style={{ minHeight: "60px" }}
                >
                    {product.description}
                </p>

                <h4 className="text-success fw-bold">
                    ₹ {product.price}
                </h4>

                <span className="badge bg-primary mb-3">
                    <FaTag /> {product.category}
                </span>

                <div className="mt-auto">

                    <button
                        className="btn btn-outline-danger w-100 mb-2"
                        onClick={addToWishlist}
                    >
                        <FaHeart /> Wishlist
                    </button>

                    <button
                        className="btn btn-success w-100 mb-2"
                        onClick={addToCart}
                    >
                        <FaShoppingCart /> Add to Cart
                    </button>

                    <button
                        className="btn btn-dark w-100"
                        onClick={() => navigate(`/product/${product.id}`)}
                    >
                        <FaEye /> View Details
                    </button>

                </div>

            </div>

        </div>

    );

}

export default ProductCard;