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
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Autocomplete
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Transportation, transportationService, Location, locationService } from '../services/api';

const DAYS = [
  { value: 1, label: 'Monday' },
  { value: 2, label: 'Tuesday' },
  { value: 3, label: 'Wednesday' },
  { value: 4, label: 'Thursday' },
  { value: 5, label: 'Friday' },
  { value: 6, label: 'Saturday' },
  { value: 7, label: 'Sunday' }
];

const TRANSPORT_TYPES = ['BUS', 'FLIGHT', 'SUBWAY', 'UBER'];

interface TransportationManagementProps {
  transportations: Transportation[];
  loading: boolean;
  onTransportationsChange: (transportations: Transportation[]) => void;
}

const TransportationManagement: React.FC<TransportationManagementProps> = ({
  transportations,
  loading,
  onTransportationsChange
}) => {
  const [locations, setLocations] = useState<Location[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingTransportation, setEditingTransportation] = useState<Transportation | null>(null);
  const [formData, setFormData] = useState<Transportation>({
    id: 0,
    originLocationId: 0,
    destinationLocationId: 0,
    transportationType: '',
    operationDays: []
  });

  useEffect(() => {
    fetchLocations();
  }, []);

  const fetchLocations = async () => {
    try {
      const response = await locationService.getAll();
      if (response.data?.success && Array.isArray(response.data?.data)) {
        setLocations(response.data.data);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error loading locations');
    }
  };

  const handleOpenDialog = (transportation?: Transportation) => {
    if (transportation) {
      setEditingTransportation(transportation);
      setFormData(transportation);
    } else {
      setEditingTransportation(null);
      setFormData({
        id: 0,
        originLocationId: 0,
        destinationLocationId: 0,
        transportationType: '',
        operationDays: []
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingTransportation(null);
    setFormData({
      id: 0,
      originLocationId: 0,
      destinationLocationId: 0,
      transportationType: '',
      operationDays: []
    });
  };

  const handleInputChange = (name: string, value: any) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingTransportation) {
        await transportationService.update(editingTransportation.id, formData);
      } else {
        await transportationService.create(formData);
      }
      onTransportationsChange(transportations);
      handleCloseDialog();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred during the operation');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this transportation?')) {
      try {
        await transportationService.delete(id);
        onTransportationsChange(transportations);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred during deletion');
      }
    }
  };

  const getDayLabel = (day: number) => {
    return DAYS.find(d => d.value === day)?.label || '';
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
            Add New Transportation
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
              <TableCell>Departure</TableCell>
              <TableCell>Arrival</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Operation Days</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transportations.map((transportation) => {
              const originLocation = locations.find(loc => loc.id === transportation.originLocationId);
              const destinationLocation = locations.find(loc => loc.id === transportation.destinationLocationId);
              return (
                <TableRow key={transportation.id}>
                  <TableCell>{originLocation?.name || 'Unknown'}</TableCell>
                  <TableCell>{destinationLocation?.name || 'Unknown'}</TableCell>
                  <TableCell>{transportation.transportationType}</TableCell>
                  <TableCell>
                    {transportation.operationDays.map(day => DAYS.find(d => d.value === day)?.label).join(', ')}
                  </TableCell>
                  <TableCell align="right">
                    <IconButton onClick={() => handleOpenDialog(transportation)} color="primary">
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(transportation.id)} color="error">
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <form onSubmit={handleSubmit}>
          <DialogTitle>
            {editingTransportation ? 'Edit Transportation' : 'Add New Transportation'}
          </DialogTitle>
          <DialogContent>
            <Autocomplete
              options={locations}
              getOptionLabel={(option) => option.name}
              value={locations.find(loc => loc.id === formData.originLocationId) || null}
              onChange={(_, newValue) => handleInputChange('originLocationId', newValue?.id || 0)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  margin="dense"
                  label="Departure Point"
                  required
                  fullWidth
                />
              )}
            />
            <Autocomplete
              options={locations}
              getOptionLabel={(option) => option.name}
              value={locations.find(loc => loc.id === formData.destinationLocationId) || null}
              onChange={(_, newValue) => handleInputChange('destinationLocationId', newValue?.id || 0)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  margin="dense"
                  label="Arrival Point"
                  required
                  fullWidth
                />
              )}
            />
            <FormControl fullWidth margin="dense">
              <InputLabel>Transportation Type</InputLabel>
              <Select
                value={formData.transportationType}
                onChange={(e) => handleInputChange('transportationType', e.target.value)}
                label="Transportation Type"
                required
              >
                {TRANSPORT_TYPES.map((type) => (
                  <MenuItem key={type} value={type}>
                    {type}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth margin="dense">
              <InputLabel>Operation Days</InputLabel>
              <Select
                multiple
                value={formData.operationDays}
                onChange={(e) => handleInputChange('operationDays', e.target.value)}
                label="Operation Days"
                renderValue={(selected) => (
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                    {(selected as number[]).map((value) => (
                      <Chip key={value} label={getDayLabel(value)} size="small" />
                    ))}
                  </Box>
                )}
                required
              >
                {DAYS.map((day) => (
                  <MenuItem key={day.value} value={day.value}>
                    {day.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
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

export default TransportationManagement; 