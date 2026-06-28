function ProductCard({ product }) {
    return (
        <div className="card shadow-sm h-100">
            <div className="card-body">

                <h5>{product.title}</h5>

                <p>{product.description}</p>

                <h4 className="text-success">
                    ₹ {product.price}
                </h4>

                <span className="badge bg-primary">
                    {product.category}
                </span>

            </div>
        </div>
    );
}

export default ProductCard;