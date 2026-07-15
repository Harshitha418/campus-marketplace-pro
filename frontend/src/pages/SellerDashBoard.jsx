import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";
import { getEmail } from "../services/auth";
import { toast } from "react-toastify";

function SellerDashboard() {

    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadMyProducts();
    }, []);

    const loadMyProducts = async () => {

        try {

            const email = getEmail();

            const response = await api.get("/products/seller", {
                params: { email }
            });

            setProducts(response.data);

        } catch (error) {

            toast.error("Could not load your products");

        } finally {

            setLoading(false);

        }

    };

    const deleteProduct = async (id) => {

        try {

            await api.delete(`/products/${id}`);

            toast.success("Product deleted");

            loadMyProducts();

        } catch (error) {

            toast.error("Could not delete product");

        }

    };

    if (loading) {
        return (
            <div className="container py-5 text-center">
                <div className="spinner-border text-primary" role="status"></div>
            </div>
        );
    }

    return (

        <div className="container py-5">

            <div className="d-flex justify-content-between align-items-center mb-4">

                <div>
                    <h2 className="fw-bold mb-1">Seller Dashboard</h2>
                    <p className="text-muted mb-0">
                        You have {products.length} product(s) listed
                    </p>
                </div>

                <Link to="/add-product" className="btn btn-dark">
                    + Add Product
                </Link>

            </div>

            {products.length === 0 ? (

                <div className="card border-0 shadow-sm rounded-4">
                    <div className="card-body text-center py-5">
                        <h5 className="text-muted">You haven't listed any products yet</h5>
                    </div>
                </div>

            ) : (

                <div className="row">

                    {products.map((product) => (

                        <div key={product.id} className="col-lg-4 col-md-6 mb-4">

                            <div className="card h-100 border-0 shadow-sm rounded-4">

                                <img
                                    src={product.imageUrl || "https://placehold.co/250x250?text=Product"}
                                    className="card-img-top p-3"
                                    style={{ height: "200px", objectFit: "contain" }}
                                    referrerPolicy="no-referrer"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = "https://placehold.co/250x250?text=Product";
                                    }}
                                />

                                <div className="card-body">

                                    <h6 className="fw-bold">{product.title}</h6>

                                    <p className="text-success fw-bold mb-2">
                                        ₹ {product.price}
                                    </p>

                                    <span className="badge bg-secondary">
                                        {product.category}
                                    </span>

                                </div>

                                <div className="card-footer bg-white border-0 d-flex gap-2">

                                    <button
                                        className="btn btn-outline-danger btn-sm w-100"
                                        onClick={() => deleteProduct(product.id)}
                                    >
                                        Delete
                                    </button>

                                </div>

                            </div>

                        </div>

                    ))}

                </div>

            )}

        </div>

    );

}

export default SellerDashboard;