import { FaHeart, FaShoppingCart } from "react-icons/fa";
import api from "../services/api";
import { getEmail } from "../services/auth";
import { Link } from "react-router-dom";
import ProductImage from "./ProductImage";
import { toast } from "react-toastify";
function ProductCard({ product }) {

    const addToWishlist = async () => {

        try {

            const email = getEmail();

            await api.post(
                `/wishlist/${product.id}`,
                null,
                {
                    params: {
                        email
                    }
                }
            );

            toast.success("❤️ Added to Wishlist!");

        } catch (error) {

            console.log(error);
            toast.error("Failed to add to Wishlist");

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
                        email
                    }
                }
            );

            toast.success("🛒 Added to Cart!");

        } catch (error) {

            console.log(error);
            toast.error("Failed to add to Cart");

        }

    };

    return (

        <div className="card shadow border-0 rounded-4 h-100">

            <div className="card-body text-center d-flex flex-column">

                <ProductImage title={product.title} imageUrl={product.imageUrl} />
                
                <Link
                    to={`/product/${product.id}`}
                    className="text-decoration-none text-dark"
                >

                    <h4 className="fw-bold">

                        {product.title}

                    </h4>

                </Link>

                <h3
                    className="text-success fw-bold my-4"
                >

                    ₹ {product.price}

                </h3>

                <div className="mt-auto">

                    <button
                        className="btn btn-outline-danger w-100 mb-2 rounded-pill"
                        onClick={addToWishlist}
                    >

                        <FaHeart /> Wishlist

                    </button>

                    <button
                        className="btn btn-success w-100 rounded-pill"
                        onClick={addToCart}
                    >

                        <FaShoppingCart /> Add to Cart

                    </button>

                </div>

            </div>

        </div>

    );

}

export default ProductCard;