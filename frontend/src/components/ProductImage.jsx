function ProductImage({ title, imageUrl }) {

    const fallback = "https://placehold.co/250x250?text=Product";

    return (
        <img
            src={imageUrl || fallback}
            alt={title}
            className="img-fluid rounded"
            onError={(e) => {
                e.target.onerror = null;
                e.target.src = fallback;
            }}
        />
    );
}

export default ProductImage;