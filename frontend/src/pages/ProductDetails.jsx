import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { FaHeart, FaShoppingCart, FaTag } from "react-icons/fa";
import { getEmail } from "../services/auth";
import { toast } from "react-toastify";
import { useCart } from "../context/CartContext";

function ProductDetails() {

    const { id } = useParams();
    const { refreshCart } = useCart();
    const [product, setProduct] = useState(null);

    useEffect(() => {

        loadProduct();

    }, []);

    const loadProduct = async () => {

        const response = await api.get(`/products/${id}`);

        setProduct(response.data);

    };

    const addToWishlist = async () => {

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

    };

    const addToCart = async () => {

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
        refreshCart();
    };

    if (!product) {

    return (

        <div
            className="d-flex flex-column justify-content-center align-items-center"
            style={{ height: "60vh" }}
        >

            <div
                className="spinner-border text-primary mb-3"
                role="status"
            >

                <span className="visually-hidden">

                    Loading...

                </span>

            </div>

            <h5 className="text-muted">

                Loading...

            </h5>

        </div>

    );

}

return (

    <div className="container py-5">

        <div className="card border-0 shadow-lg rounded-4">

            <div className="row align-items-center">

                <div className="col-lg-5 text-center p-5 border-end">

                    <img
                        src={product.imageUrl || "https://placehold.co/250x250?text=Product"}
                        className="img-fluid rounded"
                        referrerPolicy="no-referrer"
                        onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = "https://placehold.co/250x250?text=Product";
                        }}
                    />

                    <p className="text-muted mt-3">

                        Product Preview

                    </p>

                </div>

                <div className="col-lg-7">

                    <div className="card-body p-5">

                        <span className="badge bg-primary rounded-pill px-3 py-2">

                            <FaTag className="me-2" />

                            {product.category}

                        </span>

                        <h1 className="fw-bold mt-3">

                            {product.title}

                        </h1>

                        <h2 className="text-success fw-bold my-4">

                            ₹ {product.price}

                        </h2>

                        <hr />

                        <h5 className="fw-semibold">

                            Description

                        </h5>

                        <p className="text-muted">

                            {product.description}

                        </p>

                        <hr />

                        <h5 className="fw-semibold">

                            Seller

                        </h5>

                        <p>

                            {product.sellerEmail}

                        </p>

                        <div className="d-flex gap-3 mt-4">

                            <button
                                className="btn btn-outline-danger rounded-pill px-4"
                                onClick={addToWishlist}
                            >

                                <FaHeart className="me-2" />

                                Wishlist

                            </button>

                            <button
                                className="btn btn-success rounded-pill px-4"
                                onClick={addToCart}
                            >

                                <FaShoppingCart className="me-2" />

                                Add to Cart

                            </button>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    </div>

);

}

export default ProductDetails;