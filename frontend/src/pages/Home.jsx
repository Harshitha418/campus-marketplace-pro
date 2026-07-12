import { useEffect, useState } from "react";
import api from "../services/api";
import ProductCard from "../components/ProductCard";
import { toast } from "react-toastify";

function Home() {

    const [products, setProducts] = useState([]);
    const [search, setSearch] = useState("");
    const [debouncedSearch, setDebouncedSearch] = useState("");
    const [category, setCategory] = useState("All");
    const [currentPage, setCurrentPage] = useState(1);
    const productsPerPage = 9;

    useEffect(() => {
        loadProducts();
    }, []);

    // Debounce: wait until the user pauses typing for 400ms before
    // updating debouncedSearch (the value we actually filter/search with).
    // Every keystroke resets the timer via the cleanup function, so only
    // the last keystroke in a burst of typing survives.
    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearch(search);
        }, 400);

        return () => clearTimeout(timer);
    }, [search]);

    // Whenever the search or category changes, jump back to page 1 —
    // otherwise you could get stuck on page 5 of a now-empty filtered list.
    useEffect(() => {
        setCurrentPage(1);
    }, [debouncedSearch, category]);

    const loadProducts = async () => {

        try {

            const response = await api.get("/products");

            console.log(response.data);

            setProducts(response.data);

        } catch (error) {

            console.log("ERROR");
            console.log(error);
            toast.error(error.message);

        }

    };

    return (

    <div className="container py-5">

        <div className="text-center mb-5">

    <h1 className="fw-bold display-5">

        Campus Marketplace

    </h1>

    <p className="text-muted fs-5">

        Buy • Sell • Discover products within your campus

    </p>

</div>
        <div className="card shadow border-0 rounded-4 mb-4" style={{ position: "sticky", top: "80px", zIndex: 10 }}>

            <div className="card-body">

                <input
                    type="text"
                    className="form-control form-control-lg"
                    placeholder="🔍 Search for books, electronics, notes..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

            </div>

        </div>

        <div className="d-flex flex-wrap justify-content-center gap-2 mb-5">

            <button
                className={`btn ${
                    category === "All"
                        ? "btn-dark"
                        : "btn-outline-dark"
                }`}
                onClick={() => setCategory("All")}
            >
                All
            </button>

            {[...new Set(products.map((p) => p.category))].map((cat) => (
                <button
                    key={cat}
                    className={`btn ${
                        category === cat
                            ? "btn-primary"
                            : "btn-outline-primary"
                    }`}
                    onClick={() => setCategory(cat)}
                >
                    {cat}
                </button>
            ))}

        </div>

        {(() => {

            const filtered = products
                .filter((product) =>
                    product.title
                        .toLowerCase()
                        .includes(debouncedSearch.toLowerCase())
                )
                .filter((product) =>
                    category === "All"
                        ? true
                        : product.category === category
                );

            const totalPages = Math.ceil(filtered.length / productsPerPage);
            const startIndex = (currentPage - 1) * productsPerPage;
            const currentProducts = filtered.slice(startIndex, startIndex + productsPerPage);

            return (
                <>

                <div className="row">

                    {currentProducts.map((product) => (

                        <div
                            key={product.id}
                            className="col-lg-4 col-md-6 mb-4"
                        >

                            <ProductCard
                                product={product}
                            />

                        </div>

                    ))}

                </div>

                {totalPages > 1 && (
                    <div className="d-flex justify-content-center gap-2 mt-4">

                        <button
                            className="btn btn-outline-dark"
                            disabled={currentPage === 1}
                            onClick={() => setCurrentPage((p) => p - 1)}
                        >
                            Previous
                        </button>

                        <span className="align-self-center">
                            Page {currentPage} of {totalPages}
                        </span>

                        <button
                            className="btn btn-outline-dark"
                            disabled={currentPage === totalPages}
                            onClick={() => setCurrentPage((p) => p + 1)}
                        >
                            Next
                        </button>

                    </div>
                )}

                </>
            );

        })()}

    </div>

);

}

export default Home;