import { useState } from "react";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import "./NewCategory.css";
import { useCategoryMutations } from "../../hooks/useCategories";

export default function NewCategory() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: "",
        description: "",
    });

    const {
        createCategory,
        loading,
        error,
        success
    } = useCategoryMutations();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createCategory(formData);

            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Category created with success!",
                showConfirmButton: false,
                timer: 1500
            });

            // Reset form after successful creation
            setFormData({
                name: "",
            });

            setTimeout(() => {
                navigate('/categories');
            }, 1500);
        } catch (error) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: `Error while creating Category: ${error.message || 'Unknown error'}`,
                footer: '<a href="#">Why do I have this issue?</a>'
            });
        }
    };

    return (
        <div className="newUser">
            <h1 className="newUserTitle">New Category</h1>

            {error && (
                <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>
                    {error}
                </div>
            )}

            <form className="newUserForm" onSubmit={handleSubmit}>
                <div className="newUserItem">
                    <label>Category</label>
                    <input
                        type="text"
                        name="name"
                        placeholder="Category"
                        value={formData.name}
                        onChange={handleChange}
                        required
                        disabled={loading}
                    />
                </div>

                <div className="newUserItem">
                    <label>Description</label>
                    <input
                        type="text"
                        name="description"
                        placeholder="Description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                        disabled={loading}
                    />
                </div>

                <button 
                    type="submit" 
                    className="newUserButton"
                    disabled={loading}
                >
                    {loading ? 'Creation in progress...' : 'Create'}
                </button>
            </form>

            {success && (
                <div className="success-message" style={{ color: 'green', marginTop: '10px' }}>
                    Category created successfully!
                </div>
            )}
        </div>
    );
}
