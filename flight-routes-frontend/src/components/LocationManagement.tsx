import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  TextField,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
  CircularProgress
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Location, locationService } from '../services/api';

const LocationManagement: React.FC = () => {
  const [locations, setLocations] = useState<Location[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingLocation, setEditingLocation] = useState<Location | null>(null);
  const [formData, setFormData] = useState<Location>({
    name: '',
    country: '',
    locationCode: ''
  });

  useEffect(() => {
    fetchLocations();
  }, []);

  const fetchLocations = async () => {
    try {
      setLoading(true);
      const response = await locationService.getAll();
      if (response.data?.success && Array.isArray(response.data?.data)) {
        setLocations(response.data.data);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error loading locations');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (location?: Location) => {
    if (location) {
      setEditingLocation(location);
      setFormData(location);
    } else {
      setEditingLocation(null);
      setFormData({ name: '', country: '', locationCode: '' });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingLocation(null);
    setFormData({ name: '', country: '', locationCode: '' });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (editingLocation) {
        await locationService.update(editingLocation.name, formData);
      } else {
        await locationService.create(formData);
      }
      await fetchLocations();
      handleCloseDialog();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred during the operation');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (name: string) => {
    if (window.confirm('Are you sure you want to delete this location?')) {
      try {
        setLoading(true);
        await locationService.delete(name);
        await fetchLocations();
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred during deletion');
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <Box>
      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid item xs={12}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleOpenDialog()}
            sx={{ mb: 2 }}
          >
            Add New Location
          </Button>
        </Grid>
      </Grid>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Country</TableCell>
              <TableCell>Location Code</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {locations.map((location) => (
              <TableRow key={location.name}>
                <TableCell>{location.name}</TableCell>
                <TableCell>{location.country}</TableCell>
                <TableCell>{location.locationCode}</TableCell>
                <TableCell align="right">
                  <IconButton onClick={() => handleOpenDialog(location)} color="primary">
                    <EditIcon />
                  </IconButton>
                  <IconButton onClick={() => handleDelete(location.name)} color="error">
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <form onSubmit={handleSubmit}>
          <DialogTitle>
            {editingLocation ? 'Edit Location' : 'Add New Location'}
          </DialogTitle>
          <DialogContent>
            <TextField
              autoFocus
              margin="dense"
              name="name"
              label="Name"
              type="text"
              fullWidth
              value={formData.name}
              onChange={handleInputChange}
              required
            />
            <TextField
              margin="dense"
              name="country"
              label="Country"
              type="text"
              fullWidth
              value={formData.country}
              onChange={handleInputChange}
              required
            />
            <TextField
              margin="dense"
              name="locationCode"
              label="Location Code"
              type="text"
              fullWidth
              value={formData.locationCode}
              onChange={handleInputChange}
              required
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={loading}>
              {loading ? <CircularProgress size={24} /> : 'Save'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default LocationManagement; 