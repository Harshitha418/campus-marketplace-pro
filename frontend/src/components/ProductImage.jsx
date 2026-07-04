function ProductImage({ title }) {

    let image = "https://placehold.co/250x250?text=Product";

    if (title.toLowerCase().includes("book")) {
        image = "https://placehold.co/250x250?text=📚+Book";
    }
    else if (title.toLowerCase().includes("note")) {
        image = "https://placehold.co/250x250?text=📝+Notes";
    }
    else if (
        title.toLowerCase().includes("laptop") ||
        title.toLowerCase().includes("mouse") ||
        title.toLowerCase().includes("keyboard")
    ) {
        image = "https://placehold.co/250x250?text=💻+Electronics";
    }

    return (
        <img
            src={image}
            alt={title}
            className="img-fluid rounded"
        />
    );

}

export default ProductImage;