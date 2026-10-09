import { useState } from 'react';
import { Link, useLocation, NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  LayoutDashboard,
  Wallet,
  Target,
  Settings,
  LogOut,
  User,
  Menu,
  X,
  ChevronDown,
} from 'lucide-react';

export default function Navbar() {
  const { user, logout } = useAuth();
  const location = useLocation();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  const navItems = [
    { path: '/', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/expenses', label: 'Expenses', icon: Wallet },
    { path: '/incomes', label: 'Incomes', icon: Wallet },
    { path: '/budgets', label: 'Budgets', icon: Target },
  ];

  return (
    <header className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <span className="brand-icon">💰</span>
          <span className="brand-text">Spending Smarter</span>
        </Link>

        <nav className={`navbar-nav ${mobileMenuOpen ? 'open' : ''}`}>
          <ul className="nav-list">
            {navItems.map(({ path, label, icon: Icon }) => (
              <li key={path}>
                <NavLink
                  to={path}
                  className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                  onClick={() => setMobileMenuOpen(false)}
                >
                  <Icon size={20} />
                  <span>{label}</span>
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>

        <div className="navbar-actions">
          {user && (
            <div className="profile-dropdown">
              <button
                className="profile-trigger"
                onClick={() => setProfileOpen(!profileOpen)}
              >
                <div className="avatar">
                  {user.fullName?.charAt(0).toUpperCase() || user.email?.charAt(0).toUpperCase()}
                </div>
                <span className="username">{user.fullName || user.email}</span>
                <ChevronDown size={16} />
              </button>

              {profileOpen && (
                <div className="dropdown-menu">
                  <Link to="/settings" className="dropdown-item" onClick={() => setProfileOpen(false)}>
                    <User size={18} />
                    Profile
                  </Link>
                  <Link to="/settings" className="dropdown-item" onClick={() => setProfileOpen(false)}>
                    <Settings size={18} />
                    Settings
                  </Link>
                  <div className="dropdown-divider" />
                  <button className="dropdown-item danger" onClick={logout}>
                    <LogOut size={18} />
                    Logout
                  </button>
                </div>
              )}
            </div>
          )}

          {!user && (
            <div className="auth-buttons">
              <Link to="/login" className="btn btn-secondary btn-sm">Login</Link>
              <Link to="/register" className="btn btn-primary btn-sm">Get Started</Link>
            </div>
          )}

          <button
            className="mobile-menu-btn"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            aria-label="Toggle menu"
          >
            {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>
    </header>
  );
}

const styles = `
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--white);
  border-bottom: 1px solid var(--gray-light);
  box-shadow: var(--shadow);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
  font-size: 1.25rem;
  color: var(--primary);
  text-decoration: none;
}

.brand-icon {
  font-size: 1.5rem;
}

.nav-list {
  display: flex;
  align-items: center;
  gap: 4px;
  list-style: none;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: var(--radius-sm);
  color: var(--dark-light);
  font-weight: 500;
  font-size: 0.95rem;
  transition: var(--transition);
}

.nav-link:hover {
  background: var(--primary-light);
  color: var(--primary);
}

.nav-link.active {
  background: var(--primary);
  color: var(--white);
}

.nav-link.active svg {
  stroke: var(--white);
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile-dropdown {
  position: relative;
}

.profile-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px 6px 6px;
  border-radius: var(--radius-sm);
  transition: var(--transition);
}

.profile-trigger:hover {
  background: var(--gray-light);
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--primary);
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
}

.username {
  font-weight: 500;
  font-size: 0.9rem;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow-hover);
  border: 1px solid var(--gray-light);
  min-width: 200px;
  padding: 8px;
  animation: slideUp 0.2s ease;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  color: var(--dark);
  font-weight: 500;
  font-size: 0.9rem;
  transition: var(--transition);
  text-align: left;
}

.dropdown-item:hover {
  background: var(--gray-light);
}

.dropdown-item.danger {
  color: var(--danger);
}

.dropdown-item.danger:hover {
  background: #FADBD8;
}

.dropdown-divider {
  height: 1px;
  background: var(--gray-light);
  margin: 8px 0;
}

.mobile-menu-btn {
  display: none;
  padding: 8px;
  border-radius: var(--radius-sm);
  color: var(--dark);
}

.mobile-menu-btn:hover {
  background: var(--gray-light);
}

@media (max-width: 768px) {
  .navbar-nav {
    position: fixed;
    top: 64px;
    left: 0;
    right: 0;
    background: var(--white);
    border-bottom: 1px solid var(--gray-light);
    padding: 20px;
    box-shadow: var(--shadow);
    transform: translateY(-100%);
    opacity: 0;
    visibility: hidden;
    transition: var(--transition);
  }

  .navbar-nav.open {
    transform: translateY(0);
    opacity: 1;
    visibility: visible;
  }

  .nav-list {
    flex-direction: column;
    width: 100%;
  }

  .nav-link {
    width: 100%;
    justify-content: flex-start;
    padding: 14px 16px;
  }

  .auth-buttons {
    flex-direction: column;
    width: 100%;
  }

  .auth-buttons .btn {
    width: 100%;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .username {
    display: none;
  }
}
`;

// Inject styles
if (typeof document !== 'undefined') {
  const styleSheet = document.createElement('style');
  styleSheet.textContent = styles;
  document.head.appendChild(styleSheet);
}