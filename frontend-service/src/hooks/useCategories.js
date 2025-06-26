import { useState, useEffect, useCallback } from 'react';
import { categoryService } from '../services/categoryService';

export const useCategories = (initialName = '', initialPage = 0, initialSize = 10) => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pagination, setPagination] = useState({
        page: initialPage,
        size: initialSize,
        totalElements: 0,
        totalPages: 0,
    });
    const [filterName, setFilterName] = useState(initialName);

    const fetchCategories = useCallback(async (name = filterName, page = pagination.page, size = pagination.size) => {
        setLoading(true);
        setError(null);
        try {
            const response = await categoryService.getAllCategories(name, page, size);
            setCategories(response.content);
            setPagination({
                page: response.number,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
            });
        } catch (err) {
            setError(err.message || 'Failed to fetch categories');
        } finally {
            setLoading(false);
        }
    }, [filterName, pagination.page, pagination.size]);

    useEffect(() => {
        fetchCategories();
    }, [fetchCategories]);

    const handlePageChange = (newPage) => {
        setPagination((prev) => ({ ...prev, page: newPage }));
    };

    const handleFilterChange = (name) => {
        setFilterName(name);
        setPagination((prev) => ({ ...prev, page: 0 }));
    };

    return {
        categories,
        loading,
        error,
        pagination,
        filterName,
        handlePageChange,
        handleFilterChange,
        refreshCategories: fetchCategories,
    };
};

export const useCategoryDetails = (categoryId) => {
    const [category, setCategory] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchCategoryDetails = useCallback(async () => {
        if (!categoryId) return;

        setLoading(true);
        setError(null);
        try {
            const data = await categoryService.getCategoryDetails(categoryId);
            setCategory(data);
        } catch (err) {
            setError(err.message || 'Failed to fetch category details');
        } finally {
            setLoading(false);
        }
    }, [categoryId]);

    useEffect(() => {
        fetchCategoryDetails();
    }, [fetchCategoryDetails]);

    return {
        category,
        loading,
        error,
        refreshCategory: fetchCategoryDetails,
    };
};

export const useCategoryMutations = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const resetState = () => {
        setError(null);
        setSuccess(false);
    };

    const createCategory = async (categoryData) => {
        resetState();
        setLoading(true);
        try {
            const result = await categoryService.createCategory(categoryData);
            setSuccess(true);
            return result;
        } catch (err) {
            setError(err.message || 'Failed to create category');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const updateCategory = async (id, categoryData) => {
        resetState();
        setLoading(true);
        try {
            const result = await categoryService.updateCategory(id, categoryData);
            setSuccess(true);
            return result;
        } catch (err) {
            setError(err.message || 'Failed to update category');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const deleteCategory = async (id) => {
        resetState();
        setLoading(true);
        try {
            await categoryService.deleteCategory(id);
            setSuccess(true);
            return true;
        } catch (err) {
            setError(err.message || 'Failed to delete category');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    return {
        createCategory,
        updateCategory,
        deleteCategory,
        loading,
        error,
        success,
        resetState,
    };
};