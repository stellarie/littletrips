import React from 'react';
import { Provider } from 'react-redux';
import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import { store } from './store/store.tsx';
import TripHistoryPage from './pages/trip-history.page';
import SearchPage from "./pages/search.page.tsx";

const theme = createTheme({
  palette: {
    mode: 'dark',
    background: {
      default: '#080B14',
      paper: '#0E1220',
    },
    primary: { main: '#F97316' },
    secondary: { main: '#06B6D4' },
  },
  typography: {
    fontFamily: 'Sora, sans-serif',
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          background: '#080B14',
          WebkitFontSmoothing: 'antialiased',
          MozOsxFontSmoothing: 'grayscale',
        },
        '::-webkit-scrollbar': { width: 6 },
        '::-webkit-scrollbar-track': { background: '#080B14' },
        '::-webkit-scrollbar-thumb': {
          background: '#1A2035',
          borderRadius: 3,
        },
      },
    },
  },
});

const App: React.FC = () => {

  return (
      <Provider store={store}>
        <ThemeProvider theme={theme}>
          <CssBaseline/>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Navigate to="/search" replace/>}/>
              <Route path="/search" element={<SearchPage />}/>
              <Route path="/trips" element={<TripHistoryPage/>}/>
            </Routes>
          </BrowserRouter>
        </ThemeProvider>
      </Provider>
  )
};

export default App;