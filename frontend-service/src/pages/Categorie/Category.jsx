import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import Loading from "../../components/Loading/Loading";
import { useParams, useNavigate } from "react-router-dom";
import "./CategoryUpdate.css";
import { useCategoryDetails, useCategoryMutations } from "../../hooks/useCategories";

export default function Category() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: "",
        description: "",
    });

    const { 
        category, 
        loading: detailsLoading, 
        error: detailsError 
    } = useCategoryDetails(id);

    const {
        updateCategory,
        loading: updateLoading,
        error: updateError,
        success
    } = useCategoryMutations();

    useEffect(() => {
        if (category) {
            setFormData({
                name: category.name || "",
                description: category.description || "",
            });
        }
    }, [category]);

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
            await updateCategory(id, formData);

            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Category updated with success!",
                showConfirmButton: false,
                timer: 1500
            });

            setTimeout(() => {
                navigate('/categories');
            }, 1500);
        } catch (error) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: `An error has occurred: ${error.message || 'Unknown error'}`,
                footer: '<a href="#">Why do I have this issue?</a>'
            });

        }
    };

    const displayError = detailsError || updateError;

    return (
        <div className="updateUser">
            <h1 className="updateUserTitle">Edit Category</h1>

            {displayError && (
                <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>
                    {displayError}
                </div>
            )}

            {detailsLoading ? (
                <Loading />
            ) : (
                <form className="updateUserForm" onSubmit={handleSubmit}>
                    <div className="updateUserItem">
                        <label>Category</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                            disabled={updateLoading}
                        />
                        <label>Description</label>
                        <input
                            type="text"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            required
                            disabled={updateLoading}
                        />
                    </div>
                    <button 
                        type="submit" 
                        className="updateUserButton"
                        disabled={updateLoading}
                    >
                        {updateLoading ? 'Modification en cours...' : 'Modifier'}
                    </button>
                </form>
            )}
        </div>
    );

}
