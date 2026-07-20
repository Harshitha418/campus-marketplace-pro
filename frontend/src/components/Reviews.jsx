import { useEffect, useState } from "react";
import api from "../services/api";
import { toast } from "react-toastify";
import { FaStar } from "react-icons/fa";
import { isLoggedIn } from "../services/auth";

function Reviews({ productId }) {

    const [reviews, setReviews] = useState([]);
    const [rating, setRating] = useState(0);
    const [hover, setHover] = useState(0);
    const [comment, setComment] = useState("");
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        loadReviews();
    }, [productId]);

    const loadReviews = async () => {
        try {
            const res = await api.get(`/reviews/${productId}`);
            setReviews(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const submitReview = async () => {

        if (rating === 0) {
            toast.error("Please select a star rating");
            return;
        }

        setSubmitting(true);

        try {

            await api.post(`/reviews/${productId}`, { rating, comment });

            toast.success("Review added!");

            setRating(0);
            setComment("");
            loadReviews();

        } catch (err) {
            toast.error(err.response?.data?.message || "Could not add review");
        } finally {
            setSubmitting(false);
        }

    };

    const average = reviews.length
        ? (reviews.reduce((s, r) => s + r.rating, 0) / reviews.length).toFixed(1)
        : 0;

    return (

        <div className="card border-0 shadow-sm rounded-4 mt-4">

            <div className="card-body p-4">

                <div className="d-flex align-items-center justify-content-between mb-4">

                    <h4 className="fw-bold mb-0">Reviews</h4>

                    {reviews.length > 0 && (
                        <span className="fs-5">
                            <FaStar className="text-warning mb-1" /> {average}
                            <span className="text-muted fs-6"> ({reviews.length})</span>
                        </span>
                    )}

                </div>

                {/* Write a review (only if logged in) */}
                {isLoggedIn() && (

                    <div className="border rounded-3 p-3 mb-4 bg-light">

                        <p className="fw-semibold mb-2">Write a review</p>

                        <div className="mb-2">
                            {[1, 2, 3, 4, 5].map((star) => (
                                <FaStar
                                    key={star}
                                    size={26}
                                    style={{ cursor: "pointer" }}
                                    className={
                                        star <= (hover || rating)
                                            ? "text-warning me-1"
                                            : "text-secondary me-1"
                                    }
                                    onClick={() => setRating(star)}
                                    onMouseEnter={() => setHover(star)}
                                    onMouseLeave={() => setHover(0)}
                                />
                            ))}
                        </div>

                        <textarea
                            className="form-control mb-2"
                            rows="2"
                            placeholder="Share your thoughts..."
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                        />

                        <button
                            className="btn btn-dark btn-sm"
                            onClick={submitReview}
                            disabled={submitting}
                        >
                            {submitting ? "Submitting..." : "Submit Review"}
                        </button>

                    </div>

                )}

                {/* Existing reviews */}
                {reviews.length === 0 ? (

                    <p className="text-muted">No reviews yet. Be the first!</p>

                ) : (

                    reviews.map((r) => (

                        <div key={r.id} className="border-bottom py-3">

                            <div className="d-flex justify-content-between">

                                <span className="fw-semibold">{r.userEmail}</span>

                                <span>
                                    {[1, 2, 3, 4, 5].map((s) => (
                                        <FaStar
                                            key={s}
                                            size={14}
                                            className={
                                                s <= r.rating
                                                    ? "text-warning"
                                                    : "text-secondary"
                                            }
                                        />
                                    ))}
                                </span>

                            </div>

                            {r.comment && (
                                <p className="text-muted mb-0 mt-1">{r.comment}</p>
                            )}

                        </div>

                    ))

                )}

            </div>

        </div>

    );

}

export default Reviews;