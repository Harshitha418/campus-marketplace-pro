import { useEffect, useState } from "react";
import api from "../services/api";
import { toast } from "react-toastify";

function Account() {

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);

    const [form, setForm] = useState({
        name: "",
        shippingAddress: ""
    });

    const [passwordForm, setPasswordForm] = useState({
        currentPassword: "",
        newPassword: ""
    });

    const [savingProfile, setSavingProfile] = useState(false);
    const [savingPassword, setSavingPassword] = useState(false);

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {

        try {

            const response = await api.get("/users/me");

            setProfile(response.data);

            setForm({
                name: response.data.name || "",
                shippingAddress: response.data.shippingAddress || ""
            });

        } catch (error) {

            toast.error("Could not load your profile");

        } finally {

            setLoading(false);

        }

    };

    const handleFormChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handlePasswordChange = (e) => {
        setPasswordForm({ ...passwordForm, [e.target.name]: e.target.value });
    };

    const showApiError = (err, fallback) => {

        const data = err.response?.data;

        if (data?.errors) {
            Object.values(data.errors).forEach((msg) => toast.error(msg));
        } else {
            toast.error(data?.message || fallback);
        }

    };

    const saveProfile = async () => {

        setSavingProfile(true);

        try {

            const response = await api.put("/users/me", form);

            setProfile(response.data);

            toast.success("Profile updated!");

        } catch (err) {

            showApiError(err, "Could not update profile");

        } finally {

            setSavingProfile(false);

        }

    };

    const savePassword = async () => {

        setSavingPassword(true);

        try {

            await api.put("/users/me/password", passwordForm);

            toast.success("Password updated!");

            setPasswordForm({ currentPassword: "", newPassword: "" });

        } catch (err) {

            showApiError(err, "Could not update password");

        } finally {

            setSavingPassword(false);

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

            <div className="row justify-content-center">

                <div className="col-md-7">

                    <h2 className="fw-bold mb-4">My Account</h2>

                    {/* Read-only identity */}
                    <div className="card border-0 shadow-sm rounded-4 mb-4">

                        <div className="card-body p-4">

                            <div className="d-flex justify-content-between mb-2">
                                <span className="text-muted">Email</span>
                                <span className="fw-semibold">{profile?.email}</span>
                            </div>

                            <div className="d-flex justify-content-between">
                                <span className="text-muted">Account Type</span>
                                <span className="badge bg-dark align-self-center">
                                    {profile?.role}
                                </span>
                            </div>

                        </div>

                    </div>

                    {/* Editable profile */}
                    <div className="card border-0 shadow-sm rounded-4 mb-4">

                        <div className="card-body p-4">

                            <h5 className="fw-bold mb-3">Profile Details</h5>

                            <div className="mb-3">
                                <label className="form-label">Name</label>
                                <input
                                    className="form-control"
                                    name="name"
                                    value={form.name}
                                    onChange={handleFormChange}
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Shipping Address</label>
                                <textarea
                                    className="form-control"
                                    name="shippingAddress"
                                    rows="3"
                                    value={form.shippingAddress}
                                    onChange={handleFormChange}
                                    placeholder="Where should your orders be delivered?"
                                />
                            </div>

                            <button
                                className="btn btn-dark"
                                onClick={saveProfile}
                                disabled={savingProfile}
                            >
                                {savingProfile ? "Saving..." : "Save Changes"}
                            </button>

                        </div>

                    </div>

                    {/* Password change */}
                    <div className="card border-0 shadow-sm rounded-4">

                        <div className="card-body p-4">

                            <h5 className="fw-bold mb-3">Change Password</h5>

                            <div className="mb-3">
                                <label className="form-label">Current Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    name="currentPassword"
                                    value={passwordForm.currentPassword}
                                    onChange={handlePasswordChange}
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">New Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    name="newPassword"
                                    value={passwordForm.newPassword}
                                    onChange={handlePasswordChange}
                                />
                            </div>

                            <button
                                className="btn btn-outline-dark"
                                onClick={savePassword}
                                disabled={savingPassword}
                            >
                                {savingPassword ? "Updating..." : "Update Password"}
                            </button>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default Account;