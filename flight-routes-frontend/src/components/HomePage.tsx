import React, { useState } from 'react';
import {
  Box,
  Typography,
  TextField,
  Button,
  Autocomplete,
  Grid,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Alert,
  Tabs,
  Tab,
  Drawer,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { format } from 'date-fns';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import RouteIcon from '@mui/icons-material/Route';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import { Location, locationService, validRouteService, ValidRoute, transportationService, Transportation } from '../services/api';
import LocationManagement from './LocationManagement';
import TransportationManagement from './TransportationManagement';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 0 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

const HomePage: React.FC = () => {
  const [tabValue, setTabValue] = useState(0);
  const [showLocations, setShowLocations] = useState(false);
  const [showTransportations, setShowTransportations] = useState(false);
  const [locations, setLocations] = useState<Location[]>([]);
  const [transportations, setTransportations] = useState<Transportation[]>([]);
  const [originLocation, setOriginLocation] = useState<Location | null>(null);
  const [destinationLocation, setDestinationLocation] = useState<Location | null>(null);
  const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
  const [validRoutes, setValidRoutes] = useState<ValidRoute[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const drawerWidth = 240;

  React.useEffect(() => {
    fetchLocations();
  }, []);

  React.useEffect(() => {
    if (showTransportations) {
      fetchTransportations();
    }
  }, [showTransportations]);

  React.useEffect(() => {
    if (showLocations) {
      fetchLocations();
    }
  }, [showLocations]);

  const fetchLocations = async () => {
    try {
      setLoading(true);
      const response = await locationService.getAll();
      if (response.data?.success && Array.isArray(response.data?.data)) {
        setLocations(response.data.data);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Location loading failed');
    } finally {
      setLoading(false);
    }
  };

  const fetchTransportations = async () => {
    try {
      setLoading(true);
      const response = await transportationService.getAll();
      if (response.data?.success && Array.isArray(response.data?.data)) {
        setTransportations(response.data.data);
      } else {
        setError('Transportation loading failed: ' + (response.data?.message || 'Unknown error'));
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Transportation loading failed');
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleSearch = async () => {
    if (!originLocation || !destinationLocation || !selectedDate) {
      setError('Please select departure, arrival and date');
      return;
    }

    setLoading(true);
    setError(null);
    setValidRoutes([]);

    try {
      const formattedDate = format(selectedDate, 'yyyy-MM-dd');
      const response = await validRouteService.findValidRoutes(
        originLocation.id,
        destinationLocation.id,
        formattedDate
      );
      
      if (response.data?.success && Array.isArray(response.data?.data)) {
        setValidRoutes(response.data.data);
        if (response.data.data.length === 0) {
          setError('No routes found for selected locations');
        }
      } else {
        setError(response.data?.message || 'Route search operation failed');
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Route search operation failed');
      setValidRoutes([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ display: 'flex', height: '100vh' }}>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
            mt: 0,
          },
        }}
      >
        <Box sx={{ 
          overflow: 'auto', 
          mt: 0,
          borderBottom: 1,
          borderColor: 'divider',
          p: 1,
          bgcolor: 'primary.main',
          color: 'white'
        }}>
          <Typography variant="h6">
            {showLocations ? "Locations" : showTransportations ? "Transportations" : "Route Search"}
          </Typography>
        </Box>
        <Box sx={{ overflow: 'auto' }}>
          <List dense>
            <ListItem onClick={() => {
              setShowLocations(true);
              setShowTransportations(false);
            }}>
              <ListItemIcon>
                <LocationOnIcon />
              </ListItemIcon>
              <ListItemText primary="Locations" />
            </ListItem>
            <ListItem onClick={() => {
              setShowLocations(false);
              setShowTransportations(true);
            }}>
              <ListItemIcon>
                <DirectionsBusIcon />
              </ListItemIcon>
              <ListItemText primary="Transportations" />
            </ListItem>
            <ListItem onClick={() => {
              setShowLocations(false);
              setShowTransportations(false);
              setTabValue(0);
            }}>
              <ListItemIcon>
                <RouteIcon />
              </ListItemIcon>
              <ListItemText primary="Routes" />
            </ListItem>
          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', p: 0, bgcolor: 'background.default' }}>
        {showLocations ? (
          <LocationManagement />
        ) : showTransportations ? (
          <TransportationManagement 
            transportations={transportations}
            loading={loading}
            onTransportationsChange={(updatedTransportations) => {
              setTransportations(updatedTransportations);
              fetchTransportations();
            }}
          />
        ) : (
          <>
            <Box sx={{ borderBottom: 1, borderColor: 'divider', minHeight: 'auto' }}>
              <Tabs 
                value={tabValue} 
                onChange={handleTabChange}
                sx={{ minHeight: 'auto' }}
              >
                <Tab 
                  label="Route Search" 
                  sx={{ 
                    minHeight: 'auto',
                    py: 1
                  }}
                />
              </Tabs>
            </Box>

            <TabPanel value={tabValue} index={0}>
              <Grid container spacing={1} sx={{ p: 1 }}>
                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle1" sx={{ mb: 0.5 }}>
                    Departure
                  </Typography>
                  <Autocomplete
                    options={locations || []}
                    getOptionLabel={(option: Location) => option.name}
                    value={originLocation}
                    onChange={(_, newValue) => setOriginLocation(newValue)}
                    renderInput={(params) => (
                      <TextField {...params} placeholder="Select departure point" fullWidth />
                    )}
                    loading={loading}
                    loadingText="Loading locations..."
                    noOptionsText="No location found"
                  />
                </Grid>

                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle1" sx={{ mb: 0.5 }}>
                    Arrival
                  </Typography>
                  <Autocomplete
                    options={locations || []}
                    getOptionLabel={(option: Location) => option.name}
                    value={destinationLocation}
                    onChange={(_, newValue) => setDestinationLocation(newValue)}
                    renderInput={(params) => (
                      <TextField {...params} placeholder="Select arrival point" fullWidth />
                    )}
                    loading={loading}
                    loadingText="Loading locations..."
                    noOptionsText="No location found"
                  />
                </Grid>

                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle1" sx={{ mb: 0.5 }}>
                    Date
                  </Typography>
                  <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DatePicker
                      value={selectedDate}
                      onChange={(newValue) => setSelectedDate(newValue)}
                      slotProps={{
                        textField: {
                          fullWidth: true,
                          placeholder: "Select date"
                        }
                      }}
                      disablePast
                    />
                  </LocalizationProvider>
                </Grid>

                <Grid item xs={12} md={3} sx={{ display: 'flex', alignItems: 'flex-end' }}>
                  <Button
                    variant="contained"
                    onClick={handleSearch}
                    disabled={loading}
                    fullWidth
                    sx={{
                      height: 56,
                      bgcolor: '#2196f3',
                      '&:hover': {
                        bgcolor: '#1976d2',
                      },
                    }}
                  >
                    {loading ? (
                      <Box display="flex" alignItems="center" gap={1}>
                        <CircularProgress size={20} color="inherit" />
                        <span>Searching...</span>
                      </Box>
                    ) : (
                      'Search'
                    )}
                  </Button>
                </Grid>
              </Grid>

              {error && (
                <Alert severity="error" sx={{ mb: 1 }}>
                  {error}
                </Alert>
              )}

              {validRoutes.length > 0 && (
                <Box>
                  <Typography variant="h6" gutterBottom sx={{ mb: 1 }}>
                    Available Routes ({validRoutes.length} routes found)
                  </Typography>
                  <Grid container spacing={1}>
                    {validRoutes.map((route, index) => (
                      <Grid item xs={12} key={index}>
                        <Card 
                          variant="outlined" 
                          sx={{ 
                            borderRadius: 2,
                            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                            '&:hover': {
                              boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
                            },
                            bgcolor: '#fff'
                          }}
                        >
                          <CardContent sx={{ p: 2 }}>
                            <Typography variant="h6" color="primary" gutterBottom>
                              Route {index + 1}
                            </Typography>
                            <Box sx={{ mt: 1 }}>
                              {route.transportationDtoList.map((transport, tIndex) => {
                                const originLocation = locations.find(loc => loc.id === transport.originLocationId);
                                const destinationLocation = locations.find(loc => loc.id === transport.destinationLocationId);
                                return (
                                  <Box 
                                    key={transport.id}
                                    sx={{ 
                                      display: 'flex', 
                                      flexDirection: 'column',
                                      mb: tIndex !== route.transportationDtoList.length - 1 ? 2 : 0,
                                      position: 'relative',
                                      pl: 4
                                    }}
                                  >
                                    {tIndex !== route.transportationDtoList.length - 1 && (
                                      <Box
                                        sx={{
                                          position: 'absolute',
                                          left: '12px',
                                          top: '24px',
                                          bottom: '-12px',
                                          width: '2px',
                                          bgcolor: 'primary.main',
                                          opacity: 0.3
                                        }}
                                      />
                                    )}
                                    <Box sx={{ 
                                      display: 'flex', 
                                      alignItems: 'center',
                                      gap: 2,
                                      position: 'relative'
                                    }}>
                                      <Box
                                        sx={{
                                          width: 32,
                                          height: 32,
                                          borderRadius: '50%',
                                          bgcolor: 'primary.main',
                                          position: 'absolute',
                                          left: -32,
                                          display: 'flex',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          color: 'white',
                                          fontSize: '16px',
                                          fontWeight: 'bold'
                                        }}
                                      >
                                        {tIndex + 1}
                                      </Box>
                                      <Box sx={{ flex: 1 }}>
                                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                                          <Typography variant="subtitle1" sx={{ fontWeight: 500, fontSize: '1rem' }}>
                                            {originLocation?.name || 'Unknown'} → {destinationLocation?.name || 'Unknown'}
                                          </Typography>
                                          <Chip
                                            label={transport.transportationType}
                                            size="small"
                                            color="primary"
                                            sx={{ 
                                              fontWeight: 500,
                                              bgcolor: 'primary.main',
                                              color: 'white'
                                            }}
                                          />
                                        </Box>
                                      </Box>
                                    </Box>
                                  </Box>
                                );
                              })}
                            </Box>
                          </CardContent>
                        </Card>
                      </Grid>
                    ))}
                  </Grid>
                </Box>
              )}
            </TabPanel>
          </>
        )}
      </Box>
    </Box>
  );
};

export default HomePage; 