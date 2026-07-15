import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";

function AddProduct() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        title: "",
        description: "",
        price: "",
        category: "",
        imageUrl: ""
    });

    const [saving, setSaving] = useState(false);

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async () => {

        setSaving(true);

        try {

            await api.post("/products", {
                ...form,
                price: parseFloat(form.price)
            });

            toast.success("Product listed successfully!");

            navigate("/seller");

        } catch (err) {

            const data = err.response?.data;

            if (data?.errors) {
                // Validation errors from GlobalExceptionHandler
                Object.values(data.errors).forEach((msg) => toast.error(msg));
            } else {
                toast.error(data?.message || "Could not create product");
            }

        } finally {

            setSaving(false);

        }

    };

    return (

        <div className="container py-5">

            <div className="row justify-content-center">

                <div className="col-md-7">

                    <div className="card border-0 shadow rounded-4">

                        <div className="card-body p-4">

                            <h3 className="fw-bold mb-4">List a New Product</h3>

                            <div className="mb-3">
                                <label className="form-label">Title *</label>
                                <input
                                    className="form-control"
                                    name="title"
                                    value={form.title}
                                    onChange={handleChange}
                                    placeholder="e.g. Data Structures Textbook"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Description</label>
                                <textarea
                                    className="form-control"
                                    name="description"
                                    rows="3"
                                    value={form.description}
                                    onChange={handleChange}
                                    placeholder="Condition, details, etc."
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Price (₹) *</label>
                                <input
                                    className="form-control"
                                    name="price"
                                    type="number"
                                    step="0.01"
                                    value={form.price}
                                    onChange={handleChange}
                                    placeholder="499"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Category</label>
                                <input
                                    className="form-control"
                                    name="category"
                                    value={form.category}
                                    onChange={handleChange}
                                    placeholder="e.g. Computers & Tablets"
                                />
                            </div>

                            <div className="mb-4">
                                <label className="form-label">Image URL</label>
                                <input
                                    className="form-control"
                                    name="imageUrl"
                                    value={form.imageUrl}
                                    onChange={handleChange}
                                    placeholder="https://..."
                                />
                            </div>

                            <div className="d-flex gap-2">

                                <button
                                    className="btn btn-dark w-100"
                                    onClick={handleSubmit}
                                    disabled={saving}
                                >
                                    {saving ? "Listing..." : "List Product"}
                                </button>

                                <button
                                    className="btn btn-outline-secondary w-100"
                                    onClick={() => navigate("/seller")}
                                    disabled={saving}
                                >
                                    Cancel
                                </button>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default AddProduct;