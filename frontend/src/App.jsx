import { useEffect, useMemo, useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080'
});

const STATUSES = [
  'BACKLOG',
  'TO_DO',
  'IN_PROGRESS',
  'BLOCKED',
  'DONE'
];

const STATUS_LABELS = {
  BACKLOG: 'Backlog',
  TO_DO: 'To Do',
  IN_PROGRESS: 'In Progress',
  BLOCKED: 'Blocked',
  DONE: 'Done'
};

const PRIORITIES = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

function App() {
  const [token, setToken] = useState(localStorage.getItem('dreadbots-token') || '');
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('dreadbots-user');
    return storedUser ? JSON.parse(storedUser) : null;
  });

  useEffect(() => {
    if (token) {
      api.defaults.headers.common.Authorization = `Bearer ${token}`;
      localStorage.setItem('dreadbots-token', token);
    } else {
      delete api.defaults.headers.common.Authorization;
      localStorage.removeItem('dreadbots-token');
    }
  }, [token]);

  useEffect(() => {
    if (user) {
      localStorage.setItem('dreadbots-user', JSON.stringify(user));
    } else {
      localStorage.removeItem('dreadbots-user');
    }
  }, [user]);

  const handleLogin = async ({ username, password }) => {
    const response = await api.post('/api/auth/login', { username, password });
    const authUser = {
      id: response.data.id,
      username: response.data.username,
      fullName: response.data.fullName,
      role: response.data.role,
      token: response.data.token
    };
    setToken(response.data.token);
    setUser(authUser);
  };

  const handleLogout = () => {
    setToken('');
    setUser(null);
  };

  if (!token || !user) {
    return <LoginPage onLogin={handleLogin} />;
  }

  return (
    <Routes>
      <Route path="/" element={<BoardPage user={user} onLogout={handleLogout} />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

function LoginPage({ onLogin }) {
  const [form, setForm] = useState({ username: 'admin', password: 'password' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
      await onLogin(form);
    } catch (err) {
      setError(err?.response?.data?.message || 'Login failed. Check backend connection and credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Dreadbots Project Management</h1>
        <p className="subtitle">Robotics team task tracking</p>

        <form onSubmit={submit} className="login-form">
          <label>
            Username
            <input
              type="text"
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
            />
          </label>

          <label>
            Password
            <input
              type="password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />
          </label>

          {error && <div className="error-box">{error}</div>}

          <button type="submit" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>
      </div>
    </div>
  );
}

function BoardPage({ user, onLogout }) {
  const [stories, setStories] = useState([]);
  const [users, setUsers] = useState([]);
  const [newStory, setNewStory] = useState({
    summary: '',
    description: '',
    status: 'BACKLOG',
    priority: 'MEDIUM',
    assigneeId: '',
    dueDate: '',
    labelIds: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBoardData();
  }, []);

  const fetchBoardData = async () => {
    try {
      const [storiesResponse, usersResponse] = await Promise.all([
        api.get('/api/stories'),
        api.get('/api/users')
      ]);
      setStories(storiesResponse.data);
      setUsers(usersResponse.data);
    } catch (error) {
      console.error('Failed to load board data', error);
    } finally {
      setLoading(false);
    }
  };

  const rowsByStatus = useMemo(() => {
    return STATUSES.reduce((acc, status) => {
      acc[status] = stories.filter((story) => story.status === status);
      return acc;
    }, {});
  }, [stories]);

  const handleStorySubmit = async (event) => {
    event.preventDefault();

    try {
      await api.post('/api/stories', {
        ...newStory,
        assigneeId: newStory.assigneeId ? Number(newStory.assigneeId) : null,
        labelIds: newStory.labelIds.length ? newStory.labelIds.map(Number) : []
      });

      setNewStory({
        summary: '',
        description: '',
        status: 'BACKLOG',
        priority: 'MEDIUM',
        assigneeId: '',
        dueDate: '',
        labelIds: []
      });

      await fetchBoardData();
    } catch (error) {
      console.error('Failed to create story', error);
    }
  };

  const updateStoryStatus = async (storyId, status) => {
    const target = stories.find((story) => story.id === storyId);
    if (!target) return;

    try {
      await api.put(`/api/stories/${storyId}`, {
        ...target,
        status,
        assigneeId: target.assigneeId || null,
        labelIds: target.labelIds || []
      });

      await fetchBoardData();
    } catch (error) {
      console.error('Failed to update story status', error);
    }
  };

  return (
    <div className="page-shell">
      <header className="topbar">
        <div>
          <h1>Dreadbots Project Management</h1>
          <p>Welcome, {user.fullName}</p>
        </div>

        <button className="logout-button" onClick={onLogout}>
          Log out
        </button>
      </header>

      <section className="create-form-card">
        <h2>Create Story</h2>
        <form onSubmit={handleStorySubmit} className="story-form">
          <label>
            Summary
            <input
              type="text"
              value={newStory.summary}
              onChange={(e) => setNewStory({ ...newStory, summary: e.target.value })}
              required
            />
          </label>

          <label>
            Description
            <textarea
              rows="3"
              value={newStory.description}
              onChange={(e) => setNewStory({ ...newStory, description: e.target.value })}
            />
          </label>

          <div className="form-grid">
            <label>
              Status
              <select
                value={newStory.status}
                onChange={(e) => setNewStory({ ...newStory, status: e.target.value })}
              >
                {STATUSES.map((status) => (
                  <option key={status} value={status}>{STATUS_LABELS[status]}</option>
                ))}
              </select>
            </label>

            <label>
              Priority
              <select
                value={newStory.priority}
                onChange={(e) => setNewStory({ ...newStory, priority: e.target.value })}
              >
                {PRIORITIES.map((priority) => (
                  <option key={priority} value={priority}>{priority}</option>
                ))}
              </select>
            </label>

            <label>
              Assignee
              <select
                value={newStory.assigneeId}
                onChange={(e) => setNewStory({ ...newStory, assigneeId: e.target.value })}
              >
                <option value="">Unassigned</option>
                {users.map((member) => (
                  <option key={member.id} value={member.id}>{member.fullName}</option>
                ))}
              </select>
            </label>

            <label>
              Due date
              <input
                type="date"
                value={newStory.dueDate}
                onChange={(e) => setNewStory({ ...newStory, dueDate: e.target.value })}
              />
            </label>
          </div>

          <button type="submit">Add Story</button>
        </form>
      </section>

      {loading ? (
        <p>Loading board...</p>
      ) : (
        <section className="board-grid">
          {STATUSES.map((status) => (
            <div key={status} className="status-column">
              <h3>{STATUS_LABELS[status]}</h3>
              <div className="story-stack">
                {rowsByStatus[status].length === 0 ? (
                  <div className="empty-column">No stories</div>
                ) : (
                  rowsByStatus[status].map((story) => (
                    <div key={story.id} className="story-card">
                      <div className="story-header">
                        <strong>{story.summary}</strong>
                        <span className={`priority-badge priority-${story.priority.toLowerCase()}`}>
                          {story.priority}
                        </span>
                      </div>

                      <p>{story.description || 'No description provided.'}</p>

                      <div className="meta-row">
                        <span>
                          Assignee: {users.find((member) => member.id === story.assigneeId)?.fullName || 'Unassigned'}
                        </span>
                        {story.dueDate && <span>Due: {story.dueDate}</span>}
                      </div>

                      <label className="status-select-wrap">
                        Move to
                        <select
                          value={story.status}
                          onChange={(e) => updateStoryStatus(story.id, e.target.value)}
                        >
                          {STATUSES.map((option) => (
                            <option key={option} value={option}>{STATUS_LABELS[option]}</option>
                          ))}
                        </select>
                      </label>
                    </div>
                  ))
                )}
              </div>
            </div>
          ))}
        </section>
      )}
    </div>
  );
}

export default App;
